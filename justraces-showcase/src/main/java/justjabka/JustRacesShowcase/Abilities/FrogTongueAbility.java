package justjabka.JustRacesShowcase.Abilities;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect;
import justjabka.JustRaces.Abilities.Generic.ResettableAbility;
import justjabka.JustRaces.Interfaces.Configurable.AbilityConfigurable;
import justjabka.JustRaces.Managers.AbilityManager;
import justjabka.JustRaces.Managers.CombatManager;
import justjabka.JustRaces.Types.Trigger;
import justjabka.JustRaces.Types.TriggerCondition;
import justjabka.JustRacesShowcase.Abilities.Generic.BaseHookAbility;
import justjabka.JustRacesShowcase.DataProvider.DamageTypeProvider;
import justjabka.JustRacesShowcase.JustRacesShowcase;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class FrogTongueAbility extends BaseHookAbility implements ResettableAbility, AbilityConfigurable {
    private static final double STEP = 0.4;
    private static final int FIRE_TICKS = 4 * 20;
    private static final int BLOCK_COOLDOWN = 8 * 20;

    public enum TongueType {
        NORMAL(24, Color.fromRGB(255, 138, 138), BossBar.Color.PINK, true, true, false, false, false, 1f),
        SLIME(24, Color.fromRGB(126, 191, 110), BossBar.Color.GREEN, true, true, true, true, false, 1f),
        MAGMA(12, Color.fromRGB(201, 57, 6), BossBar.Color.RED, true, true, false, true, true, 3f);

        private final double maxDistance;
        private final Color color;
        @Nullable private final BossBar.Color cooldownBarColor;
        private final boolean hooksEntities;
        private final boolean hooksBlocks;
        private final boolean bypassesShields;
        private final boolean isInverted;
        private final boolean ignitesTarget;
        private final float damage;

        TongueType(
                double maxDistance,
                Color tongueColor,
                @Nullable BossBar.Color cooldownBarColor,
                boolean hooksEntities,
                boolean hooksToBlocks,
                boolean bypassesShields,
                boolean invertedHook,
                boolean ignitesEntities,
                float damageAmount
        ) {
            this.maxDistance = maxDistance;
            this.color = tongueColor;
            this.cooldownBarColor = cooldownBarColor;
            this.hooksEntities = hooksEntities;
            this.hooksBlocks = hooksToBlocks;
            this.bypassesShields = bypassesShields;
            this.isInverted = invertedHook;
            this.ignitesTarget = ignitesEntities;
            this.damage = damageAmount;
        }

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "frog_tongue");
    }

    @Override
    public long getCooldownTicks() {
        return getConfigLong("cooldown", "default");
    }

    @Override
    public Component getCooldownBarIcon(Player player) {
        return Component.text("\uE002").font(Key.key(JustRacesShowcase.NAMESPACE, "cooldown_bar"));
    }

    @Override
    public BossBar.Color getCooldownBarColor(Player player) {
        BossBar.Color cooldownBarColor = getTongueType(player).cooldownBarColor;
        return cooldownBarColor != null ? cooldownBarColor : super.getCooldownBarColor(player);
    }

    // State
    public TongueType getTongueType(Player player) {
        String hookTypeString = getContainerString(player, getKey());
        return hookTypeString != null ? TongueType.valueOf(hookTypeString.toUpperCase()) : TongueType.NORMAL;
    }

    public void setTongueType(Player player, TongueType type) {
        setContainerString(player, getKey(), type.toString());
    }

    @Override
    public void resetState(UUID pid, Reason reason) {
        if (reason == Reason.QUIT) return;

        Player player = Bukkit.getPlayer(pid);
        if (player == null) return;

        setTongueType(player, TongueType.NORMAL);
    }

    @EventHandler(ignoreCancelled = true)
    public void resetTongueType(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        if (!playerHasAbility(player)) return;

        ItemStack item = event.getItem();

        Consumable consumable = item.getData(DataComponentTypes.CONSUMABLE);
        if (consumable == null) return;

        List<ConsumeEffect> consumeEffects = consumable.consumeEffects();
        if (!consumeEffects.contains(ConsumeEffect.clearAllStatusEffects())) return;

        resetState(player, Reason.CUSTOM);
    }

    // Hook Settings
    @Override
    protected double getMaxDistance(Player shooter) {
        return getTongueType(shooter).maxDistance;
    }

    @Override
    protected boolean canHookEntity(Player shooter, LivingEntity target) {
        return getTongueType(shooter).hooksEntities;
    }

    @Override
    protected boolean canHookBlock(Player shooter, Block block) {
        return getTongueType(shooter).hooksBlocks;
    }

    @Override
    protected void renderHook(HookHitContext ctx) {
        Player shooter = ctx.shooter();
        World world = shooter.getWorld();

        TongueType tongueType = getTongueType(shooter);
        float tongueSize = getConfigFloat("size");

        Particle.DustOptions particleOptions = new Particle.DustOptions(tongueType.color, tongueSize);

        double distance = ctx.getActualDistance();
        for (double d = 0; d < distance; d += STEP) {
            Location point = ctx.startLocation().clone().add(ctx.direction().clone().multiply(d));
            world.spawnParticle(Particle.DUST, point, 1, particleOptions);
        }
    }

    // Triggers
    @Override
    public Trigger getDefaultTrigger() {
        return Trigger.OFFHAND_SWAP;
    }

    @Override
    public Set<TriggerCondition> getDefaultTriggerConditions() {
        return Set.of(TriggerCondition.SNEAKING, TriggerCondition.EMPTY_HAND);
    }

    @Override
    protected void onEntityHit(HookHitContext ctx, LivingEntity target) {
        // Get Values
        Player shooter = ctx.shooter();

        Location shooterLocation = shooter.getLocation();
        Location targetLocation = target.getLocation();

        TongueType tongueType = getTongueType(shooter);

        // SFX
        shooter.getWorld().playSound(shooterLocation, Sound.ENTITY_SLIME_ATTACK, 1.0f, 0.8f);

        long cooldownEntity = getConfigLong("cooldown", "entity");
        Bukkit.getScheduler().runTask(JustRacesShowcase.INSTANCE, () -> setCooldownTicks(shooter, cooldownEntity));

        // Easter Egg
        if (tryConsumeMobEasterEgg(shooter, target)) return;

        // Damage Target
        boolean hitBlocked = applyDamageAndAndCheckBlock(shooter, target, tongueType);

        // Ignite Target
        if (tongueType.ignitesTarget) {
            target.setFireTicks(FIRE_TICKS);
        }

        if (hitBlocked && !tongueType.bypassesShields) return;

        // Hook
        double multiplier = ctx.getActualDistance() * 0.18 + 0.25;

        if (tongueType.isInverted) {
            applyImpulse(shooter, shooterLocation, targetLocation, multiplier, 0.3);
        } else {
            applyImpulse(target, targetLocation, shooterLocation, multiplier, 0.3);
        }
    }

    private boolean applyDamageAndAndCheckBlock(Player shooter, LivingEntity target, TongueType tongueType) {
        if (tongueType.damage <= 0) return false;

        DamageSource tongueDamageSource = DamageSource.builder(DamageTypeProvider.FROG_TONGUE)
                .withCausingEntity(shooter)
                .withDirectEntity(shooter)
                .build();

        if (target instanceof Player targetPlayer) {
            int cooldown = tongueType.bypassesShields ? BLOCK_COOLDOWN : 0;
            return CombatManager.attackAndTryDisableBlock(targetPlayer, cooldown, tongueType.damage, tongueDamageSource);
        }

        target.damage(tongueType.damage, tongueDamageSource);
        return false;
    }

    @Override
    protected void onBlockHit(HookHitContext ctx, Block block) {
        Player shooter = ctx.shooter();
        applyImpulse(shooter, shooter.getLocation(), Objects.requireNonNull(ctx.getHitLocation()), 1.8, 0.4);
        shooter.getWorld().playSound(shooter.getLocation(), Sound.ENTITY_FROG_TONGUE, 1.0f, 1.0f);

        long cooldownBlock = getConfigLong("cooldown", "block");
        Bukkit.getScheduler().runTask(JustRacesShowcase.INSTANCE, () -> setCooldownTicks(shooter, cooldownBlock));
    }

    // Easter Egg
    private boolean tryConsumeMobEasterEgg(Player shooter, LivingEntity target) {
        if (target instanceof MagmaCube magmaCube) {
            setTongueType(shooter, TongueType.MAGMA);
            magmaCube.remove();
            playEatenEffects(shooter, Sound.ENTITY_MAGMA_CUBE_DEATH);
            return true;
        } else if (target instanceof Slime slime) {
            setTongueType(shooter, TongueType.SLIME);
            slime.remove();
            playEatenEffects(shooter, Sound.ENTITY_SLIME_DEATH);
            return true;
        }

        return false;
    }

    private void playEatenEffects(Player shooter, Sound deathSound) {
        World world = shooter.getWorld();
        world.playSound(shooter.getLocation(), deathSound, 1.0f, 1.2f);
        world.playSound(shooter.getLocation(), Sound.ENTITY_FROG_EAT, 1.0f, 0.9f);
    }
}
