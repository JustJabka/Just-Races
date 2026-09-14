package justjabka.justraces.api.races.generic;

import justjabka.justraces.api.common.PlayerTickRunnable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class BaseRaceRunnable extends BukkitRunnable implements PlayerTickRunnable, Race {

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!isRequiredRace(player)) continue;

            onTick(player);
        }
    }
}
