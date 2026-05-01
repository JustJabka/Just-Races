package justjabka.WeltenRaces.Runnables;

import org.bukkit.Bukkit;
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
        if (isGliding) return;

        removeWings(player);
        this.cancel();
    }
}
