package justjabka.JustRacesShowcase.Runnables.Ability;

import justjabka.JustRaces.Abilities.Generic.BaseValidationAbility;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class UnfoldWingsAbilityRunnable extends BukkitRunnable {
    private final BaseValidationAbility ability;
    private final UUID pid;

    public UnfoldWingsAbilityRunnable(BaseValidationAbility ability, UUID pid) {
        this.ability = ability;
        this.pid = pid;
    }

    @Override
    public void run() {
        Player player = Bukkit.getPlayer(pid);

        if (player == null) {
            this.cancel();
            return;
        }

        if (!ability.isStateValid(player)) {
            ability.onDeactivation(player);
            this.cancel();
            return;
        }

        whileGliding(player);
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
