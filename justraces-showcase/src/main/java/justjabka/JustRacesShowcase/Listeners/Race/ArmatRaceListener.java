package justjabka.JustRacesShowcase.Listeners.Race;

import io.papermc.paper.event.entity.EntityEquipmentChangedEvent;
import justjabka.JustRaces.Interfaces.Configurable.RaceConfigurable;
import justjabka.JustRaces.Listeners.Generic.BaseRaceListener;
import justjabka.JustRaces.Managers.ArmorManager;
import justjabka.JustRaces.Types.ArmorSet;
import justjabka.JustRacesShowcase.DataProvider.DamageTypeProvider;
import justjabka.JustRacesShowcase.DataProvider.DamageTypeTagKeysProvider;
import justjabka.JustRacesShowcase.DataProvider.RaceProvider;
import justjabka.JustRacesShowcase.JustRacesShowcase;
import org.bukkit.*;
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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;

import java.util.Random;

public class ArmatRaceListener extends BaseRaceListener implements RaceConfigurable {
    private static final Random RANDOM = new Random();

    private static final NamespacedKey IGNORE_POTION_KEY = new NamespacedKey(JustRacesShowcase.NAMESPACE, "ignore_potion");
    private static final Particle.Spell ABSOLUTE_DAMAGE_PARTICLE = new Particle.Spell(
            Color.fromARGB(255,210,252,243),
            1f
    );

    @Override
    public NamespacedKey getKey() {
        return RaceProvider.ARMAT;
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!isRequiredRace(player)) return;

        DamageSource damageSource = event.getDamageSource();
        Entity causingEntity = damageSource.getCausingEntity();

        double damage = event.getDamage();
        DamageType damageType = damageSource.getDamageType();

        if (handleDamageCauses(event, damageType, player)) return;
        boolean successfullyDodged = handleDodge(event, player, damageType);

        handleArmorSetBonusesOnDamage(event, player, damage, causingEntity, damageType, successfullyDodged);
    }

    private boolean handleDamageCauses(EntityDamageEvent event, DamageType damageType, Player player) {
        EntityDamageEvent.DamageCause damageCause = event.getCause();

        boolean isVulnerableTo = DamageTypeTagKeysProvider.getTagValues(DamageTypeTagKeysProvider.IS_MAGIC).contains(damageType);
        boolean isImmuneTo = damageCause == EntityDamageEvent.DamageCause.FALL && ArmorManager.hasAnyArmor(player);

        if (isVulnerableTo) {
            return true;
        } else if (isImmuneTo) {
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, SoundCategory.PLAYERS, 0.5f, 1.5f);
            event.setCancelled(true);
            return true;
        }

        return false;
    }

    private boolean handleDodge(EntityDamageEvent event, Player player, DamageType damageType) {
        double dodgeChance;
        double maxDodgeChance = getConfigDouble("damage_dodge", "max_chance");

        // Calc Dodge Chance
        AttributeInstance luckInstance = player.getAttribute(Attribute.LUCK);
        if (luckInstance == null) return false;

        dodgeChance = luckInstance.getValue() * 0.1;
        dodgeChance = Math.clamp(dodgeChance, 0, maxDodgeChance);

        // Try Dodge
        if (RANDOM.nextDouble() > dodgeChance) return false;

        // Dodge
        if (DamageTypeTagKeysProvider.getTagValues(DamageTypeTagKeysProvider.BYPASSES_DODGE).contains(damageType)) return false;

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 2f, 0.8f);
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
        double startingPoint = getConfigDouble("damage_reduction", "starting_point");
        double damageMultiplier = getConfigDouble("damage_reduction", "damage_multiplier");

        if (damage < startingPoint) return;

        double finalDamage = damage * damageMultiplier;
        event.setDamage(finalDamage);
    }

    private void handleChainmailArmorSetBonus(Player player, double damage, Entity causingEntity, DamageType damageType, boolean successfullyDodged) {
        // Parry Logic
        if (!successfullyDodged) return;
        if (!(causingEntity instanceof LivingEntity attacker)) return;

        double parryDamagePercent = getConfigDouble("damage_dodge", "parry", "damage_percent");
        int parryArmorPenalty = getConfigInt("damage_dodge", "parry", "armor_penalty");

        double parryDamage = damage * parryDamagePercent;
        DamageSource parrySource = DamageSource.builder(damageType)
                .withCausingEntity(player)
                .withDirectEntity(player)
                .build();

        attacker.damage(parryDamage, parrySource);

        // Parry Armor Damage Penalty
        for (ItemStack armor : player.getEquipment().getArmorContents()) {
            if (armor == null) continue;
            armor.damage(parryArmorPenalty, player);
        }

        // SFX
        World world = player.getWorld();
        Location location = player.getLocation();

        world.playSound(location, Sound.ENTITY_PLAYER_ATTACK_STRONG, SoundCategory.PLAYERS, 2f, 1f);
        world.playSound(location, Sound.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.PLAYERS, 2f, 1f);
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity victim)) return;
        if (!(event.getDamager() instanceof Player attacker)) return;

        if (!isRequiredRace(attacker)) return;
        if (ArmorManager.getArmorSet(attacker) != ArmorSet.DIAMOND) return;

        double absoluteDamageAmount = getConfigDouble("absolute_damage", "amount");
        float absoluteDamageCooldown = getConfigFloat("absolute_damage", "min_attack_cooldown");

        if (attacker.getAttackCooldown() < absoluteDamageCooldown) return;

        // Prevent stack overflow
        if (event.getDamageSource().getDamageType() == DamageTypeProvider.ABSOLUTE_DAMAGE) return;

        DamageSource absoluteDamageSource = DamageSource.builder(DamageTypeProvider.ABSOLUTE_DAMAGE)
                .withCausingEntity(attacker)
                .withDirectEntity(attacker)
                .build();

        victim.damage(absoluteDamageAmount, absoluteDamageSource);

        victim.getWorld().spawnParticle(
                Particle.INSTANT_EFFECT,
                victim.getEyeLocation().subtract(0, 0.5, 0),
                1,
                0.25,
                0.5,
                0.25,
                ABSOLUTE_DAMAGE_PARTICLE
        );
    }

    @EventHandler(ignoreCancelled = true)
    public void onArmorChange(EntityEquipmentChangedEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (!isRequiredRace(player)) return;

        applyBoundShellBonus(player);
        applyCopperArmorBonus(player);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();

        if (!isRequiredRace(player)) return;
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

    @EventHandler(ignoreCancelled = true)
    public void onPotionApply(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (!isRequiredRace(player)) return;
        if (ArmorManager.getArmorSet(player) != ArmorSet.GOLDEN) return;

        EntityPotionEffectEvent.Action action = event.getAction();
        if (action != EntityPotionEffectEvent.Action.ADDED && action != EntityPotionEffectEvent.Action.CHANGED) return;

        double effectDurationMultiplier = getConfigDouble("alchemy", "effect_duration_multiplier");
        JustRacesShowcase.LOGGER.info(String.valueOf(effectDurationMultiplier));

        PersistentDataContainer pdc = player.getPersistentDataContainer();

        // Prevent stack overflow
        if (pdc.has(IGNORE_POTION_KEY)) {
            pdc.remove(IGNORE_POTION_KEY);
            return;
        }

        PotionEffect effect = event.getNewEffect();
        if (effect == null) return;

        event.setCancelled(true);

        pdc.set(IGNORE_POTION_KEY, PersistentDataType.BOOLEAN, true);

        int newDuration = (int) (effect.getDuration() * effectDurationMultiplier);

        effect.withDuration(newDuration).apply(player);
    }

    private void applyBoundShellBonus(Player player) {
        // Get Attributes
        AttributeInstance maxHealthInstance = player.getAttribute(Attribute.MAX_HEALTH);
        AttributeInstance armorInstance = player.getAttribute(Attribute.ARMOR);

        if (maxHealthInstance == null || armorInstance == null) return;

        double armorValue = armorInstance.getValue();

        // Delete old attribute
        maxHealthInstance.removeModifier(getKey());

        // Calc new attribute
        if (armorValue <= 0) return;

        AttributeModifier modifier = new AttributeModifier(
                getKey(),
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
        miningEfficiencyInstance.removeModifier(getKey());

        if (ArmorManager.getArmorSet(player) != ArmorSet.COPPER) return;

        double miningBonusMax = getConfigDouble("mining_bonus", "upper_bound");
        double miningBonusStep = getConfigDouble("mining_bonus", "step");

        // Calc new attribute
        double durability = ArmorManager.getAverageDurability(player);
        double maxDurability = 1.0;

        double miningBonus = Math.min(miningBonusMax, Math.floor((maxDurability - durability) / miningBonusStep));

        // Apply new attribute
        if (miningBonus <= 0) return;

        AttributeModifier modifier = new AttributeModifier(
                getKey(),
                miningBonus,
                AttributeModifier.Operation.ADD_NUMBER
        );
        miningEfficiencyInstance.addModifier(modifier);
    }
}