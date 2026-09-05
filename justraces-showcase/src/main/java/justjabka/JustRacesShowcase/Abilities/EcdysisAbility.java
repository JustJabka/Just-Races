package justjabka.JustRacesShowcase.Abilities;

import justjabka.JustRaces.Abilities.Generic.BaseAbility;
import justjabka.JustRaces.Abilities.Generic.DurationAbility;
import justjabka.JustRaces.Interfaces.Configurable.AbilityConfigurable;
import justjabka.JustRaces.Managers.AbilityManager;
import justjabka.JustRaces.Managers.ArmorManager;
import justjabka.JustRaces.Managers.AttributeManager;
import justjabka.JustRaces.Types.AbilityContext;
import justjabka.JustRaces.Types.ArmorSet;
import justjabka.JustRaces.Types.Trigger;
import justjabka.JustRaces.Types.TriggerCondition;
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

public class EcdysisAbility extends BaseAbility implements DurationAbility, AbilityConfigurable {

    private final Set<PotionEffect> userEffects = Set.of(
            new PotionEffect(PotionEffectType.RESISTANCE, (int) getConfigDuration(), 4, false, true),
            new PotionEffect(PotionEffectType.SPEED, (int) getConfigDuration(), 1, false, true)
    );
    private final Map<Attribute, AttributeModifier> userModifiers = Map.of(
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

    private final Map<UUID, BukkitTask> chainTasks = new ConcurrentHashMap<>();
    private final Map<UUID, BukkitTask> effectTasks = new ConcurrentHashMap<>();

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "ecdysis");
    }

    @Override
    public long getCooldownTicks() {
        return getConfigCooldown();
    }

    @Override
    public long getDurationTicks() {
        return getConfigDuration();
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
    public Trigger getDefaultTrigger() {
        return Trigger.OFFHAND_SWAP;
    }

    @Override
    public Set<TriggerCondition> getDefaultTriggerConditions() {
        return Set.of(TriggerCondition.SNEAKING, TriggerCondition.EMPTY_HAND);
    }

    @Override
    protected boolean canActivate(Player player) {
        return ArmorManager.getArmorSet(player) == ArmorSet.NETHERITE;
    }

    @Override
    protected boolean onActivation(Player player, AbilityContext ctx) {
        if (isSuicideUse(player)) handleSuicideUse(player);
        else handleNormalUse(player);

        return true;
    }

    @Override
    public void resetState(UUID pid, Reason reason) {
        cancelTasks(pid);

        Player player = Bukkit.getPlayer(pid);
        if (player == null) return;

        AttributeManager.removeModifiers(player, userModifiers);
        userEffects.forEach(effect -> player.removePotionEffect(effect.getType()));
        onChainExpire(player);
    }

    private boolean isSuicideUse(Player player) {
        double suicideDurabilityPercent = getConfigDouble("suicide_durability_percent");
        double avrgDurability = ArmorManager.getAverageDurability(player);

        return avrgDurability <= suicideDurabilityPercent;
    }

    private void handleNormalUse(Player player) {
        int nextChain = getNextChain(player);
        int effectDuration = Math.toIntExact(getDurationTicks() * nextChain);
        UUID pid = player.getUniqueId();

        damageArmor(player);

        // Apply effects
        userEffects.forEach(effect ->
                player.addPotionEffect(effect.withDuration(effectDuration))
        );
        if (!AttributeManager.hasModifiers(player, userModifiers)) {
            AttributeManager.addModifiers(player, userModifiers);
        }

        createExplosion(player, Material.NETHERITE_BLOCK, getConfigFloat("explosion_power", "normal"));

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
        int maxChain = getConfigInt("max_chain_amount");
        int currentChain = AbilityManager.getAbilityValue(player, getKey());
        int nextChain = Math.clamp(currentChain + 1, 1, maxChain);

        AbilityManager.setAbilityValue(player, getKey(), nextChain);
        return nextChain;
    }

    private void handleSuicideUse(Player player) {
        DamageSource damageSource = DamageSource.builder(DamageType.PLAYER_EXPLOSION).withDirectEntity(player).withDamageLocation(player.getLocation()).build();
        player.damage(Integer.MAX_VALUE, damageSource);
        createExplosion(player, Material.REDSTONE_BLOCK, getConfigFloat("explosion_power", "suicide"));
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
        float durabilityPercentPerUse = getConfigFloat("durability_percent_per_use");

        for (ItemStack armor : player.getEquipment().getArmorContents()) {
            if (armor == null) continue;
            if (!(armor.getItemMeta() instanceof Damageable meta)) continue;

            int maxDamage = meta.hasMaxDamage()
                    ? meta.getMaxDamage()
                    : armor.getType().getMaxDurability();

            int damagePerUse = (int) (maxDamage * durabilityPercentPerUse);
            int minDurability = maxDamage - 1;
            int newDurability = meta.getDamage() + damagePerUse;

            meta.setDamage(Math.min(newDurability, minDurability));
            armor.setItemMeta(meta);
        }
    }
}