package justjabka.WeltenRaces.Listeners;

import io.papermc.paper.event.entity.EntityEquipmentChangedEvent;
import justjabka.WeltenRaces.Configs.Race.ArmatRaceConfig;
import justjabka.WeltenRaces.DataProvider.DamageTypeProvider;
import justjabka.WeltenRaces.Managers.ArmorManager;
import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.Types.ArmorSet;
import justjabka.WeltenRaces.Types.Race;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;

import java.util.Random;
import java.util.Set;

import static justjabka.WeltenRaces.DataProvider.DamageTypeProvider.ABSOLUTE_DAMAGE_KEY;
import static justjabka.WeltenRaces.DataProvider.DamageTypeProvider.BYPASSES_DODGE_TAG;

public class ArmatRaceListener implements Listener {
    private final ArmatRaceConfig config;

    public ArmatRaceListener(ArmatRaceConfig config) {
        this.config = config;
    }

    private static final Random RANDOM = new Random();

    private static final NamespacedKey BOUND_SHELL_KEY = new NamespacedKey(WeltenRaces.NAMESPACE, "bound_shell");
    private static final NamespacedKey IGNORE_POTION_KEY = new NamespacedKey(WeltenRaces.NAMESPACE, "ignore_potion");
    private static final NamespacedKey COPPER_MINING_EFFICIENCY_KEY = new NamespacedKey(WeltenRaces.NAMESPACE, "copper_mining_efficiency");


    private static final Set<EntityDamageEvent.DamageCause> IMMUNE_TO = Set.of(
            EntityDamageEvent.DamageCause.FALL
    );
    private static final Set<EntityDamageEvent.DamageCause> VULNERABLE_TO = Set.of(
            EntityDamageEvent.DamageCause.MAGIC,
            EntityDamageEvent.DamageCause.POISON,
            EntityDamageEvent.DamageCause.WITHER,
            EntityDamageEvent.DamageCause.THORNS
    );

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (RaceManager.getRace(player) != Race.ARMAT) return;

        DamageSource damageSource = event.getDamageSource();
        Entity causingEntity = damageSource.getCausingEntity();

        double damage = event.getDamage();
        DamageType damageType = damageSource.getDamageType();
        EntityDamageEvent.DamageCause damageCause = event.getCause();

        if (handleDamageCauses(event, player, damageCause)) return;
        boolean successfullyDodged = handleDodge(event, player, damageType);

        handleArmorSetBonusesOnDamage(event, player, damage, causingEntity, damageType, successfullyDodged);
    }

    private boolean handleDamageCauses(EntityDamageEvent event, Player player, EntityDamageEvent.DamageCause damageCause) {
        if (VULNERABLE_TO.contains(damageCause)) {
            // TODO: break boots depending on damage taken
            event.setDamage(event.getDamage() * config.vulnerableMultiplier);
            return true;
        } else if (IMMUNE_TO.contains(damageCause) && ArmorManager.hasAnyArmor(player)) {
            // TODO: remove ts and use attribute instead
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, SoundCategory.PLAYERS, 0.5f, 1.5f);
            event.setCancelled(true);
            return true;
        }
        return false;
    }

    private boolean handleDodge(EntityDamageEvent event, Player player, DamageType damageType) {
        double dodgeChance;

        // Calc Dodge Chance
        AttributeInstance luckInstance = player.getAttribute(Attribute.LUCK);
        if (luckInstance == null) return false;

        dodgeChance = luckInstance.getValue() * 0.1;
        dodgeChance = Math.clamp(dodgeChance, 0, config.maxDodgeChance);

        // Try Dodge
        if (RANDOM.nextDouble() > dodgeChance) return false;

        // Dodge
        if (DamageTypeProvider.getTagValues(BYPASSES_DODGE_TAG).contains(damageType)) return false;

        event.setCancelled(true);
        return true;
    }

    private void handleArmorSetBonusesOnDamage(EntityDamageEvent event, Player player, double damage, Entity causingEntity, DamageType damageType, boolean successfullyDodged) {
        if (ArmorManager.getArmorSet(player) == ArmorSet.NONE) return;

        switch (ArmorManager.getArmorSet(player)) {
            case CHAINMAIL -> handleChainmailArmorSetBonus(player, damage, causingEntity, damageType, successfullyDodged);
            case IRON -> handleIronArmorSetBonus(event, damage);
        }
    }

    private void handleIronArmorSetBonus(EntityDamageEvent event, double damage) {
        if (damage < config.reductionStart) return;

        double finalDamage = damage * config.reductionMultiplier;
        event.setDamage(finalDamage);
    }

    private void handleChainmailArmorSetBonus(Player player, double damage, Entity causingEntity, DamageType damageType, boolean successfullyDodged) {
        // Parry Logic
        if (!successfullyDodged) return;
        if (!(causingEntity instanceof LivingEntity attacker)) return;

        double parryDamage = damage * config.parryDamagePercent;
        DamageSource parrySource = DamageSource.builder(damageType)
                .withCausingEntity(player)
                .withDirectEntity(player)
                .build();

        attacker.damage(parryDamage, parrySource);

        // Parry Armor Damage Penalty
        for (ItemStack armor : player.getEquipment().getArmorContents()) {
            if (armor == null) continue;
            armor.damage(config.parryArmorPenalty, player);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity victim)) return;
        if (!(event.getDamager() instanceof Player attacker)) return;

        if (RaceManager.getRace(attacker) != Race.ARMAT) return;
        if (ArmorManager.getArmorSet(attacker) != ArmorSet.DIAMOND) return;

        if (attacker.getAttackCooldown() < config.absoluteDamageCooldown) return;

        DamageType absoluteDamageType = DamageTypeProvider.getKey(ABSOLUTE_DAMAGE_KEY);

        // Prevent stack overflow
        if (event.getDamageSource().getDamageType() == absoluteDamageType) return;

        DamageSource absoluteDamageSource = DamageSource.builder(absoluteDamageType)
                .withCausingEntity(attacker)
                .withDirectEntity(attacker)
                .build();

        victim.damage(config.absoluteDamageAmount, absoluteDamageSource);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!event.hasChangedBlock()) return;

        Player player = event.getPlayer();

        if (RaceManager.getRace(player) != Race.ARMAT) return;

        AttributeInstance gravityInstance = player.getAttribute(Attribute.GRAVITY);
        if (gravityInstance == null) return;

        boolean shouldSink = player.isInWater() && ArmorManager.hasAnyArmor(player);
        boolean hasModifier = gravityInstance.getModifier(BOUND_SHELL_KEY) != null;

        if (shouldSink && !hasModifier) {
            AttributeModifier modifier = new AttributeModifier(
                    BOUND_SHELL_KEY,
                    config.sinkGravity,
                    AttributeModifier.Operation.ADD_NUMBER
            );

            gravityInstance.addModifier(modifier);
        } else if (!shouldSink && hasModifier) {
            gravityInstance.removeModifier(BOUND_SHELL_KEY);
        }
    }

    @EventHandler
    public void onArmorChange(EntityEquipmentChangedEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (RaceManager.getRace(player) != Race.ARMAT) return;

        applyBoundShellBonus(player);
        applyCopperArmorBonus(player);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();

        if (RaceManager.getRace(player) != Race.ARMAT) return;
        if (ArmorManager.getArmorSet(player) != ArmorSet.GOLDEN) return;

        ItemStack consumedItem = event.getItem();

        Material goldenVersion = switch (consumedItem.getType()) {
            case APPLE -> Material.GOLDEN_APPLE;
            case CARROT -> Material.GOLDEN_CARROT;
            default -> null;
        };

        if (goldenVersion == null) return;

        ItemStack resultItem = new ItemStack(goldenVersion).asOne();

        event.setItem(resultItem);
        event.setReplacement(consumedItem.subtract());

        double current = player.getAbsorptionAmount();
        double bonus = (goldenVersion == Material.GOLDEN_APPLE) ? 4.0 : 0;
        double limit = player.getAttribute(Attribute.MAX_ABSORPTION).getValue();

        player.setAbsorptionAmount(Math.min(current + bonus, limit));
    }

    @EventHandler
    public void onPotionApply(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (RaceManager.getRace(player) != Race.ARMAT) return;
        if (ArmorManager.getArmorSet(player) != ArmorSet.GOLDEN) return;

        EntityPotionEffectEvent.Action action = event.getAction();
        if (action != EntityPotionEffectEvent.Action.ADDED && action != EntityPotionEffectEvent.Action.CHANGED) return;

        PersistentDataContainer pdc = player.getPersistentDataContainer();

        // Prevent stack overflow
        if (pdc.has(IGNORE_POTION_KEY)) {
            pdc.remove(IGNORE_POTION_KEY);
            return;
        }

        PotionEffect effect = event.getNewEffect();
        if (effect == null) return;

        // TODO: FIX TS PLS
        event.setCancelled(true);

        pdc.set(IGNORE_POTION_KEY, PersistentDataType.BOOLEAN, true);

        int newDuration = (int) (effect.getDuration() * config.effectDurationMultiplier);

        effect.withDuration(newDuration).apply(player);
    }

    private static void applyBoundShellBonus(Player player) {
        // Get Attributes
        AttributeInstance maxHealthInstance = player.getAttribute(Attribute.MAX_HEALTH);
        AttributeInstance armorInstance = player.getAttribute(Attribute.ARMOR);

        if (maxHealthInstance == null || armorInstance == null) return;

        double armorValue = armorInstance.getValue();

        // Delete old attribute
        maxHealthInstance.removeModifier(BOUND_SHELL_KEY);

        // Calc new attribute
        if (armorValue <= 0) return;

        AttributeModifier modifier = new AttributeModifier(
                BOUND_SHELL_KEY,
                armorValue,
                AttributeModifier.Operation.ADD_NUMBER
        );
        maxHealthInstance.addModifier(modifier);
    }

    private void applyCopperArmorBonus(Player player) {
        // Get Attributes
        AttributeInstance miningEfficiencyInstance = player.getAttribute(Attribute.MINING_EFFICIENCY);
        if (miningEfficiencyInstance == null) return;

        // Delete old attribute
        miningEfficiencyInstance.removeModifier(COPPER_MINING_EFFICIENCY_KEY);

        if (ArmorManager.getArmorSet(player) != ArmorSet.COPPER) return;

        // Calc new attribute
        double durability = ArmorManager.getAverageDurability(player);
        double maxDurability = 1.0;

        double miningBonus = Math.min(config.miningBonusMax, Math.floor((maxDurability - durability) / config.miningBonusStep));

        // Apply new attribute
        if (miningBonus <= 0) return;

        AttributeModifier modifier = new AttributeModifier(
                COPPER_MINING_EFFICIENCY_KEY,
                miningBonus,
                AttributeModifier.Operation.ADD_NUMBER
        );
        miningEfficiencyInstance.addModifier(modifier);
    }
}