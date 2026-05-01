package justjabka.WeltenRaces.Runnables;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.World;
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
        World world = player.getWorld();

        double wingSpanFactor = 0.6;

        double boundingBoxWidth = player.getBoundingBox().getWidthX();
        double offset = boundingBoxWidth * wingSpanFactor;

        double angle = Math.toRadians(player.getYaw());

        double offsetX = Math.cos(angle) * offset;
        double offsetZ = Math.sin(angle) * offset;

        world.spawnParticle(
                Particle.MYCELIUM,
                player.getX() + offsetX,
                player.getY(),
                player.getZ() + offsetZ,
                1
        );

        world.spawnParticle(
                Particle.MYCELIUM,
                player.getX() - offsetX,
                player.getY(),
                player.getZ() - offsetZ,
                1
        );
    }
}
