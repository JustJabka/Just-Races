package justjabka.justraces.api.runnables.generic;

import justjabka.justraces.api.interfaces.RaceContext;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class BaseRaceRunnable extends BukkitRunnable implements PlayerTickRunnable, RaceContext {

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!isRequiredRace(player)) continue;

            onTick(player);
        }
    }
}
