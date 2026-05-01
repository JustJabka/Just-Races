package justjabka.WeltenRaces.Runnables;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

import static justjabka.WeltenRaces.Abilities.UnfoldWings.removeWings;

public class UnfoldWingsAbilityRunnable extends BukkitRunnable {
    private final UUID pid;

    public UnfoldWingsAbilityRunnable(UUID pid) {
        this.pid = pid;
    }

    @Override
    public void run() {
        Player player = Bukkit.getPlayer(pid);

        if (player == null) {
            this.cancel();
            return;
        }

        boolean isGliding = player.isGliding();

        if (isGliding) {
            whileGliding(player);
            return;
        }

        removeWings(player);
        this.cancel();
    }

    private static void whileGliding(Player player) {
        player.getWorld().spawnParticle(
                Particle.MYCELIUM,
                player.getBoundingBox().getMaxX(),
                player.getLocation().getY(),
                player.getLocation().getZ(),
                1
        );
        player.getWorld().spawnParticle(
                Particle.MYCELIUM,
                player.getBoundingBox().getMinX(),
                player.getLocation().getY(),
                player.getLocation().getZ(),
                1
        );
    }
}
