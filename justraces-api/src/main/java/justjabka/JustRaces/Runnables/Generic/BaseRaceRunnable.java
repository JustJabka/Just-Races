package justjabka.JustRaces.Runnables.Generic;

import justjabka.JustRaces.Interfaces.Configurable.RaceConfigurable;
import justjabka.JustRaces.Interfaces.RaceContext;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class BaseRaceRunnable extends BukkitRunnable implements RaceContext, RaceConfigurable {

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!isRequiredRace(player)) continue;

            onTick(player);
        }
    }

    public abstract void onTick(Player player);
}
