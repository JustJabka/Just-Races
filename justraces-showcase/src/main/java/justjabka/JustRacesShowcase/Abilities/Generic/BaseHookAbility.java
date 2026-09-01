package justjabka.JustRacesShowcase.Abilities.Generic;

import justjabka.JustRaces.Abilities.Generic.BaseAbility;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public abstract class BaseHookAbility extends BaseAbility {

    public record HookHitContext(
            Player shooter,
            Location startLocation,
            Vector direction,
            double maxDistance,
            RayTraceResult rayTraceResult
    ) {
        public boolean hasHit() {
            return rayTraceResult != null;
        }

        public Location getHitLocation() {
            return rayTraceResult != null ? rayTraceResult.getHitPosition().toLocation(shooter.getWorld()) : null;
        }

        public LivingEntity getHitEntity() {
            return (rayTraceResult != null && rayTraceResult.getHitEntity() instanceof LivingEntity living) ? living : null;
        }

        public Block getHitBlock() {
            return rayTraceResult != null ? rayTraceResult.getHitBlock() : null;
        }

        public double getActualDistance() {
            Location hitLoc = getHitLocation();
            return hitLoc != null ? startLocation.distance(hitLoc) : maxDistance;
        }
    }

    protected abstract double getMaxDistance(Player shooter);
    protected abstract boolean canHookEntity(Player shooter, LivingEntity target);
    protected abstract boolean canHookBlock(Player shooter, Block block);

    protected abstract void renderHook(HookHitContext ctx);

    protected abstract void onEntityHit(HookHitContext ctx, LivingEntity target);
    protected abstract void onBlockHit(HookHitContext ctx, Block block);
    protected void onMiss(HookHitContext ctx) {}

    @Override
    protected boolean onActivation(Player shooter) {
        World world = shooter.getWorld();
        Location startLoc = shooter.getEyeLocation();
        Vector direction = startLoc.getDirection().normalize();

        double maxDistance = getMaxDistance(shooter);

        RayTraceResult result = world.rayTrace(
                startLoc,
                direction,
                maxDistance,
                FluidCollisionMode.NEVER,
                true,
                0.5,
                entity -> !entity.equals(shooter) && entity instanceof LivingEntity
        );

        HookHitContext hitContext = new HookHitContext(shooter, startLoc, direction, maxDistance, result);

        renderHook(hitContext);

        if (!hitContext.hasHit()) {
            onMiss(hitContext);
            return true;
        }

        LivingEntity target = hitContext.getHitEntity();
        Block hitBlock = hitContext.getHitBlock();

        if (target != null && canHookEntity(shooter, target)) {
            onEntityHit(hitContext, target);
        } else if (hitBlock != null && canHookBlock(shooter, hitBlock)) {
            onBlockHit(hitContext, hitBlock);
        }

        return true;
    }

    protected void applyImpulse(LivingEntity entityToMove, Location origin, Location destination, double multiplier, double yBonus) {
        Vector pullVector = destination.toVector().subtract(origin.toVector()).normalize().multiply(multiplier);
        pullVector.setY(pullVector.getY() + yBonus);
        entityToMove.setVelocity(pullVector);
    }
}
