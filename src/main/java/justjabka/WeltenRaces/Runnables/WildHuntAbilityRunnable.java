package justjabka.WeltenRaces.Runnables;

import justjabka.WeltenRaces.Abilities.WildHunt;
import justjabka.WeltenRaces.Configs.Abilities.WildHuntConfig;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class WildHuntAbilityRunnable extends BukkitRunnable {
    private final WildHuntConfig config;
    private final UUID playerId;
    private final UUID victimId;

    public WildHuntAbilityRunnable(WildHuntConfig config, UUID playerId, UUID victimId) {
        this.config = config;
        this.playerId = playerId;
        this.victimId = victimId;
    }

    @Override
    public void run() {
        Player player = Bukkit.getPlayer(playerId);
        LivingEntity victim = (LivingEntity) Bukkit.getEntity(victimId);

        if (victim == null) {
            this.cancel();
            return;
        }

        if (player == null) {
            removeEffects(victim);
            return;
        }

        boolean isVictimInRadius = player.getLocation().distanceSquared(victim.getLocation()) < (config.radius * config.radius);
        if (!isVictimInRadius) {
            removeEffects(victim);
            return;
        }

        onUseEffects(victim);
    }

    private void removeEffects(LivingEntity victim) {
        WildHunt.VICTIM_EFFECTS.forEach(e -> victim.removePotionEffect(e.getType()));
        this.cancel();
    }

    private static void onUseEffects(LivingEntity victim) {
        victim.getWorld().spawnParticle(
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
