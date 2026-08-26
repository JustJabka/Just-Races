package justjabka.JustRacesShowcase.Abilities;

import justjabka.JustRaces.Abilities.Generic.BaseAbility;
import justjabka.JustRaces.Managers.AbilityManager;
import justjabka.JustRaces.Managers.ArmorManager;
import justjabka.JustRaces.Managers.AttributeManager;
import justjabka.JustRaces.Types.ArmorSet;
import justjabka.JustRacesShowcase.Configs.Ability.EcdysisAbilityConfig;
import justjabka.JustRacesShowcase.JustRacesShowcase;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class EcdysisAbility extends BaseAbility {
    private final EcdysisAbilityConfig config;

    private final Set<PotionEffect> userEffects;
    private final Map<Attribute, AttributeModifier> userModifiers;

    // TODO: move constants to the ability config
    private static final float DAMAGE_PERCENT_PER_USE = 0.25f;
    private static final float NORMAL_USE_EXPLOSION_POWER = 6f; // end crystal
    private static final float SUICIDE_USE_EXPLOSION_POWER = 7f; // wither birth

    private static final int MAX_CHAIN_AMOUNT = 6;

    public EcdysisAbility(EcdysisAbilityConfig config) {
        this.config = config;
        this.userEffects = Set.of(
                new PotionEffect(PotionEffectType.RESISTANCE, config.effectDuration, 4, false, true),
                new PotionEffect(PotionEffectType.SPEED, config.effectDuration, 1, false, true)
        );
        this.userModifiers = Map.of(
                Attribute.KNOCKBACK_RESISTANCE, new AttributeModifier(
                        getKey(),
                        1024,
                        AttributeModifier.Operation.ADD_NUMBER
                ),
                Attribute.EXPLOSION_KNOCKBACK_RESISTANCE, new AttributeModifier(
                        getKey(),
                        1024,
                        AttributeModifier.Operation.ADD_NUMBER
                )
        );
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "ecdysis");
    }

    @Override
    public long getCooldownTicks() {
        return config.cooldown;
    }

    @Override
    public BossBar.Color getCooldownBarColor(Player player) {
        return isSuicideUse(player) ? BossBar.Color.RED : BossBar.Color.PURPLE;
    }

    // TODO: draw icon
    @Override
    public Component getCooldownBarIcon(Player player) {
        return super.getCooldownBarIcon(player);
    }

    @Override
    protected boolean canActivate(Player player) {
        return ArmorManager.getArmorSet(player) == ArmorSet.NETHERITE;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (AbilityManager.getAbilityValue(player, getKey()) == 0) return;
        breakUseChain(player);
    }

    @EventHandler(ignoreCancelled = true)
    public void trigger(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();

        if (!player.isSneaking()) return;
        if (!player.getInventory().getItemInMainHand().isEmpty()) return;

        tryActivate(player);
        event.setCancelled(true);
    }

    @Override
    protected boolean onActivation(Player player, Object... ctx) {
        if (isSuicideUse(player)) handleSuicideUse(player);
        else handleNormalUse(player);

        return true;
    }

    private boolean isSuicideUse(Player player) {
        double avrgDurability = ArmorManager.getAverageDurability(player);
        return avrgDurability <= config.suicideDurabilityPercent;
    }

    private void handleNormalUse(Player player) {
        int nextChain = getNextChain(player);
        int scaledDurationTicks = config.effectDuration * nextChain;
        UUID pid = player.getUniqueId();

        damageArmor(player);

        // Apply effects
        userEffects.forEach(effect ->
                player.addPotionEffect(effect.withDuration(scaledDurationTicks))
        );
        if (!AttributeManager.hasModifiers(player, userModifiers)) {
            AttributeManager.addModifiers(player, userModifiers);
        }

        createExplosion(player, Material.NETHERITE_BLOCK, NORMAL_USE_EXPLOSION_POWER);

        Bukkit.getScheduler().runTaskLater(JustRacesShowcase.INSTANCE, () -> {
            // Get Player
            Player plr = Bukkit.getPlayer(pid);
            if (plr == null) return;

            // Get active chain
            int activeChain = AbilityManager.getAbilityValue(plr, getKey());
            if (activeChain != nextChain) return;

            breakUseChain(plr);
        }, config.effectDuration);
    }

    private void breakUseChain(Player player) {
        AbilityManager.setAbilityValue(player, getKey(), 0);
        AttributeManager.removeModifiers(player, userModifiers);
    }

    private int getNextChain(Player player) {
        int currentChain = AbilityManager.getAbilityValue(player, getKey());
        int nextChain = Math.clamp(currentChain + 1, 1, MAX_CHAIN_AMOUNT);
        AbilityManager.setAbilityValue(player, getKey(), nextChain);
        return nextChain;
    }

    private static void handleSuicideUse(Player player) {
        DamageSource damageSource = DamageSource.builder(DamageType.PLAYER_EXPLOSION).withDirectEntity(player).withDamageLocation(player.getLocation()).build();
        player.damage(Integer.MAX_VALUE, damageSource);
        createExplosion(player, Material.REDSTONE_BLOCK, SUICIDE_USE_EXPLOSION_POWER);
    }

    private static void createExplosion(Player player, Material material, float power) {
        World world = player.getWorld();
        Location location = player.getLocation();
        Location explosionLocation = location.add(0, 1, 0);

        world.createExplosion(explosionLocation, power, false, false, player);

        // Particles
        world.spawnParticle(
                Particle.EXPLOSION_EMITTER,
                explosionLocation,
                1,
                0,
                0,
                0,
                1
        );
        world.spawnParticle(
                Particle.BLOCK,
                explosionLocation,
                100,
                2.5,
                1,
                2.5,
                Bukkit.createBlockData(material)
        );

        // Sounds
        world.playSound(location, Sound.ENTITY_WITHER_BREAK_BLOCK, SoundCategory.PLAYERS, 1, 2);
        world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 2, 1);
    }

    private static void damageArmor(Player player) {
        for (ItemStack armor : player.getEquipment().getArmorContents()) {
            if (armor == null) continue;
            if (!(armor.getItemMeta() instanceof Damageable meta)) continue;

            int maxDamage = meta.hasMaxDamage()
                    ? meta.getMaxDamage()
                    : armor.getType().getMaxDurability();

            int damagePerUse = (int) (maxDamage * DAMAGE_PERCENT_PER_USE);
            int minDurability = maxDamage - 1;
            int newDurability = meta.getDamage() + damagePerUse;

            meta.setDamage(Math.min(newDurability, minDurability));
            armor.setItemMeta(meta);
        }
    }
}