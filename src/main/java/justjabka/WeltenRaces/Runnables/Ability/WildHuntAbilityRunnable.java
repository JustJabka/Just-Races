package justjabka.WeltenRaces.Runnables.Ability;

import justjabka.WeltenRaces.Abilities.WildHuntAbility;
import justjabka.WeltenRaces.Configs.Abilities.WildHuntConfig;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class WildHuntAbilityRunnable extends BukkitRunnable {
    private final UUID attackerId;
    private final UUID victimId;
    private final double radiusSquared;

    public WildHuntAbilityRunnable(WildHuntConfig config, UUID playerId, UUID victimId) {
        this.attackerId = playerId;
        this.victimId = victimId;
        this.radiusSquared = config.radius * config.radius;
    }

    @Override
    public void run() {
        Player attacker = Bukkit.getPlayer(attackerId);
        Player victim = Bukkit.getPlayer(victimId);

        if (victim == null) {
            this.cancel();
            return;
        }

        UUID currentOwner = WildHuntAbility.getCurrentOwner(victim);
        if (currentOwner == null || !currentOwner.equals(attackerId)) {
            this.cancel();
            return;
        }

        if (attacker == null) {
            WildHuntAbility.clearAbility(victim);

            this.cancel();
            return;
        }

        boolean isVictimInRadius = attacker.getLocation().distanceSquared(victim.getLocation()) < radiusSquared;
        if (!isVictimInRadius) {
            WildHuntAbility.clearAbility(victim);

            this.cancel();
            return;
        }

        onUseEffects(attacker, victim);
    }

    private static void onUseEffects(Player attacker, Player victim) {
        attacker.spawnParticle(
                Particle.END_ROD,
                victim.getX(),
                victim.getBoundingBox().getCenterY(),
                victim.getZ(),
                10,
                0.25,
                0.5,
                0.25,
                0.01
        );
    }
}
