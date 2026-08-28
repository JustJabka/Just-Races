package justjabka.JustRacesShowcase.Abilities;

import justjabka.JustRaces.Abilities.Generic.BaseAbility;
import justjabka.JustRaces.Abilities.Generic.BaseDurationAbility;
import justjabka.JustRaces.Managers.AbilityManager;
import justjabka.JustRaces.Managers.ArmorManager;
import justjabka.JustRaces.Managers.AttributeManager;
import justjabka.JustRaces.Types.ArmorSet;
import justjabka.JustRacesShowcase.Configs.Ability.EcdysisAbilityConfig;
import justjabka.JustRacesShowcase.JustRacesShowcase;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EcdysisAbility extends BaseAbility implements BaseDurationAbility {
    private final EcdysisAbilityConfig config;

    private final Set<PotionEffect> userEffects;
    private final Map<Attribute, AttributeModifier> userModifiers;

    private final Map<UUID, BukkitTask> chainTasks = new ConcurrentHashMap<>();
    private final Map<UUID, BukkitTask> effectTasks = new ConcurrentHashMap<>();

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
    public int getDurationTicks() {
        return config.effectDuration;
    }

    @Override
    public BossBar.Color getCooldownBarColor(Player player) {
        return isSuicideUse(player) ? BossBar.Color.RED : BossBar.Color.PURPLE;
    }

    @Override
    public Component getCooldownBarIcon(Player player) {
        return Component.text("\uE001").font(Key.key(JustRacesShowcase.NAMESPACE, "cooldown_bar"));
    }

    @Override
    protected boolean canActivate(Player player) {
        return ArmorManager.getArmorSet(player) == ArmorSet.NETHERITE;
    }

    @EventHandler(ignoreCancelled = true)
    public void trigger(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();

        if (!player.isSneaking()) return;
        if (!player.getInventory().getItemInMainHand().isEmpty()) return;

        if (!tryActivate(player)) return;
        event.setCancelled(true);
    }

    @Override
    protected boolean onActivation(Player player, Object... ctx) {
        if (isSuicideUse(player)) handleSuicideUse(player);
        else handleNormalUse(player);

        return true;
    }

    @Override
    public void resetState(UUID pid) {
        cancelTasks(pid);

        Player player = Bukkit.getPlayer(pid);
        if (player == null) return;

        AttributeManager.removeModifiers(player, userModifiers);
        userEffects.forEach(effect -> player.removePotionEffect(effect.getType()));
        onChainExpire(player);
    }

    private boolean isSuicideUse(Player player) {
        double avrgDurability = ArmorManager.getAverageDurability(player);
        return avrgDurability <= config.suicideDurabilityPercent;
    }

    private void handleNormalUse(Player player) {
        int nextChain = getNextChain(player);
        int effectDuration = getDurationTicks() * nextChain;
        UUID pid = player.getUniqueId();

        damageArmor(player);

        // Apply effects
        userEffects.forEach(effect ->
                player.addPotionEffect(effect.withDuration(effectDuration))
        );
        if (!AttributeManager.hasModifiers(player, userModifiers)) {
            AttributeManager.addModifiers(player, userModifiers);
        }

        createExplosion(player, Material.NETHERITE_BLOCK, config.explosionPowerNormal);

        cancelTasks(pid);
        BukkitScheduler scheduler = Bukkit.getScheduler();

        BukkitTask chainTask = scheduler.runTaskLater(JustRacesShowcase.INSTANCE, () -> {
            onChainExpire(player);
            chainTasks.remove(pid);
        }, getDurationTicks());
        chainTasks.put(pid, chainTask);

        BukkitTask effectTask = scheduler.runTaskLater(JustRacesShowcase.INSTANCE, () -> {
            onExpire(player);
            effectTasks.remove(pid);
        }, effectDuration);
        effectTasks.put(pid, effectTask);
    }

    private int getNextChain(Player player) {
        int currentChain = AbilityManager.getAbilityValue(player, getKey());
        int nextChain = Math.clamp(currentChain + 1, 1, config.maxChainAmount);
        AbilityManager.setAbilityValue(player, getKey(), nextChain);
        return nextChain;
    }

    private void handleSuicideUse(Player player) {
        DamageSource damageSource = DamageSource.builder(DamageType.PLAYER_EXPLOSION).withDirectEntity(player).withDamageLocation(player.getLocation()).build();
        player.damage(Integer.MAX_VALUE, damageSource);
        createExplosion(player, Material.REDSTONE_BLOCK, config.explosionPowerSuicide);
    }

    private void cancelTasks(UUID pid) {
        BukkitTask chainTask = chainTasks.remove(pid);
        if (chainTask != null) chainTask.cancel();

        BukkitTask effectTask = effectTasks.remove(pid);
        if (effectTask != null) effectTask.cancel();
    }

    private void onChainExpire(Player player) {
        AbilityManager.setAbilityValue(player, getKey(), 0);
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

    private void damageArmor(Player player) {
        for (ItemStack armor : player.getEquipment().getArmorContents()) {
            if (armor == null) continue;
            if (!(armor.getItemMeta() instanceof Damageable meta)) continue;

            int maxDamage = meta.hasMaxDamage()
                    ? meta.getMaxDamage()
                    : armor.getType().getMaxDurability();

            int damagePerUse = (int) (maxDamage * config.durabilityPercentPerUse);
            int minDurability = maxDamage - 1;
            int newDurability = meta.getDamage() + damagePerUse;

            meta.setDamage(Math.min(newDurability, minDurability));
            armor.setItemMeta(meta);
        }
    }
}