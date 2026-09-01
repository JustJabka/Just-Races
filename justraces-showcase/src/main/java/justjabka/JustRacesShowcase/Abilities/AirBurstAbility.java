package justjabka.JustRacesShowcase.Abilities;

import justjabka.JustRaces.Abilities.Generic.BaseAbility;
import justjabka.JustRaces.Interfaces.Configurable.AbilityConfigurable;
import justjabka.JustRaces.Managers.CombatManager;
import justjabka.JustRacesShowcase.JustRacesShowcase;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NonNull;

public class AirBurstAbility extends BaseAbility implements AbilityConfigurable {
    private static final double ADDITIONAL_Y = 0.35;
    private  static final double DOT = 0.45;

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "air_burst");
    }

    @Override
    public long getCooldownTicks() {
        return getConfigCooldown();
    }

    @EventHandler(ignoreCancelled = true)
    public void onTrigger(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();

        if (!player.isSneaking()) return;
        if (!player.getInventory().getItemInMainHand().isEmpty()) return;

        if (!tryActivate(player)) return;
        event.setCancelled(true);
    }

    @Override
    protected boolean onActivation(Player player, Object... ctx) {
        World world = player.getWorld();
        Location playerLocation = player.getLocation();
        Location playerEyeLocation = player.getEyeLocation();

        Vector lookDirection = playerEyeLocation.getDirection().normalize();
        
        Vector pushVector = calcVelocity(lookDirection, getConfigDouble("push", "multiplier"));
        Vector recoilVector = calcVelocity(
                lookDirection,
                player.isOnGround() ? getConfigDouble("recoil", "normal_multiplier")
                                    : getConfigDouble("recoil", "airborne_multiplier")
        );

        burst(player, playerEyeLocation, playerLocation, lookDirection, pushVector, recoilVector);

        // On Use Effects
        world.spawnParticle(Particle.GUST_EMITTER_LARGE, playerLocation, 1);
        world.playSound(playerLocation, Sound.ENTITY_BREEZE_WIND_BURST, SoundCategory.PLAYERS, 1f, 1f);

        return true;
    }

    private void burst(
            Player player,
            Location playerEyeLocation,
            Location playerLocation,
            Vector lookDirection,
            Vector pushVector,
            Vector recoilVector
    ) {
        double radius = getConfigDouble("radius");
        for (LivingEntity target : playerEyeLocation.getNearbyLivingEntities(radius)) {
            if (target.equals(player)) continue;

            // Get distance between player and target
            Location targetLocation = target.getLocation();
            Vector toTarget = targetLocation.toVector()
                    .subtract(playerLocation.toVector())
                    .normalize();

            // Ignore blind spot
            double dot = lookDirection.dot(toTarget);
            if (dot < DOT) continue;

            // Damage target
            double damage = getConfigDouble("damage");
            DamageSource damageSource = DamageSource.builder(DamageType.WIND_CHARGE)
                    .withCausingEntity(player)
                    .withDirectEntity(player)
                    .build();
            target.damage(damage, damageSource);

            // Apply velocity
            Vector finalPush = decreasePush(target, pushVector, damage, damageSource);
            target.setVelocity(finalPush);
        }

        player.setVelocity(recoilVector);
    }

    private @NonNull Vector decreasePush(LivingEntity target, Vector pushVector, double damage, DamageSource damageSource) {
        Vector finalPush = pushVector.clone();
        AttributeInstance knockbackResistanceInstance = target.getAttribute(Attribute.KNOCKBACK_RESISTANCE);

        if (knockbackResistanceInstance != null) {
            final double maxKnockBackResistance = 1;
            double knockbackResistance = knockbackResistanceInstance.getValue();

            finalPush.multiply(maxKnockBackResistance - knockbackResistance);
        }

        if (target instanceof Player targetPlayer && CombatManager.attackAndTryDisableBlock(targetPlayer, 0, damage, damageSource)) {
            double blockedPushMultiplier = getConfigDouble("push", "blocked_multiplier");
            finalPush.multiply(blockedPushMultiplier);
        }

        return finalPush;
    }

    private static @NonNull Vector calcVelocity(Vector lookDirection, double pushMultiplier) {
        Vector pushVector = lookDirection.clone().multiply(pushMultiplier);
        pushVector.setY(Math.max(pushVector.getY(), ADDITIONAL_Y));
        
        return pushVector;
    }
}
