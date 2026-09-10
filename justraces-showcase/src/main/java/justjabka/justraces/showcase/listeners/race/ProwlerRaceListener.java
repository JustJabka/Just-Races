package justjabka.justraces.showcase.listeners.race;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import com.destroystokyo.paper.entity.ai.MobGoals;
import justjabka.justraces.api.listeners.generic.BaseRaceListener;
import justjabka.justraces.api.managers.RaceManager;
import justjabka.justraces.showcase.dataprovider.RaceProvider;
import justjabka.justraces.showcase.JustRacesShowcase;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.EnumSet;

public class ProwlerRaceListener extends BaseRaceListener {

    @Override
    public NamespacedKey getKey() {
        return RaceProvider.PROWLER;
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageByBlockEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!isRequiredRace(player)) return;

        DamageType damageType = event.getDamageSource().getDamageType();
        if (!damageType.equals(DamageType.SWEET_BERRY_BUSH)) return;
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (!((event.getEntity()) instanceof Chicken chicken)) return;

        Server server = chicken.getServer();
        MobGoals mobGoals = server.getMobGoals();

        addAvoidProwlerRaceGoal(chicken, mobGoals);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntitiesLoad(EntitiesLoadEvent event) {
        for (Entity entity : event.getEntities()) {
            if (!(entity instanceof Chicken chicken)) continue;

            MobGoals mobGoals = chicken.getServer().getMobGoals();
            if (mobGoals.hasGoal(chicken, ChickenAvoidProwlerRaceGoal.KEY)) continue;

            addAvoidProwlerRaceGoal(chicken, mobGoals);
        }
    }

    private static void addAvoidProwlerRaceGoal(Chicken chicken, MobGoals mobGoals) {
        mobGoals.addGoal(chicken, 3, new ChickenAvoidProwlerRaceGoal(chicken));
    }

    @NullMarked
    public static class ChickenAvoidProwlerRaceGoal implements Goal<Chicken> {
        public static final GoalKey<Chicken> KEY = GoalKey.of(
                Chicken.class,
                new NamespacedKey(JustRacesShowcase.NAMESPACE, "chicken_avoid_prowler_race")
        );

        private static final double FLEE_DISTANCE = 12 * 12;

        private final Chicken chicken;
        private @Nullable Player targetProwler;

        public ChickenAvoidProwlerRaceGoal(Chicken chicken) {
            this.chicken = chicken;
        }

        @Override
        public boolean shouldActivate() {
            for (Entity entity : chicken.getNearbyEntities(8, 8, 8)) {
                if (!(entity instanceof Player player)) continue;
                if (!RaceManager.isRace(player, RaceProvider.PROWLER)) continue;

                this.targetProwler = player;
                return true;
            }

            this.targetProwler = null;
            return false;
        }

        @Override
        public boolean shouldStayActive() {
            if (targetProwler == null || !targetProwler.isOnline() || !targetProwler.isValid()) {
                return false;
            }

            return chicken.getLocation().distanceSquared(targetProwler.getLocation()) < FLEE_DISTANCE;
        }

        @Override
        public void start() {
            moveToFleeLocation();
        }

        @Override
        public void tick() {
            if (chicken.getTicksLived() % 5 != 0) return;
            moveToFleeLocation();
        }

        @Override
        public void stop() {
            this.targetProwler = null;
            chicken.getPathfinder().stopPathfinding();
        }

        private void moveToFleeLocation() {
            if (targetProwler == null) return;

            Location chickenLoc = chicken.getLocation();
            Location playerLoc = targetProwler.getLocation();

            Vector fleeDirection = chickenLoc.toVector().subtract(playerLoc.toVector()).normalize();

            Location fleeTarget = chickenLoc.clone().add(fleeDirection.multiply(8));

            chicken.getPathfinder().moveTo(fleeTarget, 1.5);
        }

        @Override
        public GoalKey<Chicken> getKey() {
            return KEY;
        }

        @Override
        public EnumSet<GoalType> getTypes() {
            return EnumSet.of(GoalType.MOVE, GoalType.LOOK);
        }
    }
}
