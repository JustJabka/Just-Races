package justjabka.JustRacesShowcase.Abilities;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect;
import justjabka.JustRaces.Abilities.Generic.ResettableAbility;
import justjabka.JustRaces.Managers.AbilityManager;
import justjabka.JustRacesShowcase.Abilities.Generic.BaseHookAbility;
import justjabka.JustRacesShowcase.Configs.Ability.FrogTongueAbilityConfig;
import justjabka.JustRacesShowcase.JustRacesShowcase;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("UnstableApiUsage")
public class FrogTongueAbility extends BaseHookAbility implements ResettableAbility {
    private final FrogTongueAbilityConfig config;

    private static final double STEP = 0.4;

    public FrogTongueAbility(FrogTongueAbilityConfig config) {
        this.config = config;
    }

    public enum TongueType {
        NORMAL(24, Color.fromRGB(255, 138, 138), true, true, true, false, false, false, 1f),
        SLIME(24, Color.fromRGB(126, 191, 110), true, true, false, true, true, false, 0f),
        MAGMA(12, Color.fromRGB(201, 57, 6), true, true, false, false, true, true, 3f);

        private final double maxDistance;
        private final Color color;
        private final boolean hooksEntities;
        private final boolean hooksBlocks;
        private final boolean canBeCanceled; // TODO
        private final boolean bypassesShields; // TODO
        private final boolean isInverted;
        private final boolean ignitesTarget;
        private final float damage;

        TongueType(
                double maxDistance,
                Color tongueColor,
                boolean hooksEntities,
                boolean hooksToBlocks,
                boolean canBeCanceled,
                boolean bypassesShields,
                boolean invertedHook,
                boolean ignitesEntities,
                float damageAmount
        ) {
            this.maxDistance = maxDistance;
            this.color = tongueColor;
            this.hooksEntities = hooksEntities;
            this.hooksBlocks = hooksToBlocks;
            this.canBeCanceled = canBeCanceled;
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
        return config.cooldownDefault;
    }

    @Override
    public BossBar.Color getCooldownBarColor(Player player) {
        return BossBar.Color.PINK;
    }

    // State
    public TongueType getTongueType(Player player) {
        String hookTypeString = AbilityManager.getAbilityString(player, getKey());
        return hookTypeString != null ? TongueType.valueOf(hookTypeString.toUpperCase()) : TongueType.NORMAL;
    }

    public void setTongueType(Player player, TongueType type) {
        AbilityManager.setAbilityString(player, getKey(), type.toString());
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
        Particle.DustOptions particleOptions = new Particle.DustOptions(tongueType.color, config.size);

        double distance = ctx.getActualDistance();
        for (double d = 0; d < distance; d += STEP) {
            Location point = ctx.startLocation().clone().add(ctx.direction().clone().multiply(d));
            world.spawnParticle(Particle.DUST, point, 1, particleOptions);
        }
    }

    // Triggers
    @EventHandler(ignoreCancelled = true)
    public void onTrigger(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();

        if (!player.isSneaking()) return;
        if (!player.getInventory().getItemInMainHand().isEmpty()) return;

        if (!tryActivate(player)) return;
        event.setCancelled(true);
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

        Bukkit.getScheduler().runTask(JustRacesShowcase.INSTANCE, () -> setCooldownTicks(shooter, config.cooldownEntity));

        // Easter Egg
        if (tryConsumeMobEasterEgg(shooter, target)) return;

        // Damage Target
        if (tongueType.damage > 0) {
            DamageSource hookDamage = DamageSource.builder(DamageType.PLAYER_ATTACK).withDirectEntity(shooter).build();
            target.damage(tongueType.damage, hookDamage);
        }

        // Ignite Target
        if (tongueType.ignitesTarget) {
            target.setFireTicks(80);
        }

        // Hook
        double multiplier = ctx.getActualDistance() * 0.18 + 0.25;

        if (tongueType.isInverted) {
            applyImpulse(shooter, shooterLocation, targetLocation, multiplier, 0.3);
        } else {
            applyImpulse(target, targetLocation, shooterLocation, multiplier, 0.3);
        }
    }

    @Override
    protected void onBlockHit(HookHitContext ctx, Block block) {
        Player shooter = ctx.shooter();
        applyImpulse(shooter, shooter.getLocation(), Objects.requireNonNull(ctx.getHitLocation()), 1.8, 0.4);
        shooter.getWorld().playSound(shooter.getLocation(), Sound.ENTITY_FROG_TONGUE, 1.0f, 1.0f);

        Bukkit.getScheduler().runTask(JustRacesShowcase.INSTANCE, () -> setCooldownTicks(shooter, config.cooldownBlock));
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
