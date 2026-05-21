package justjabka.WeltenRaces.Runnables.Generic;

import justjabka.WeltenRaces.Managers.RaceManager;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class BaseRaceRunnable extends BukkitRunnable {
    public abstract NamespacedKey getRaceKey();

    public boolean isRequiredRace(Player player) {
        return RaceManager.isRace(player, getRaceKey());
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!isRequiredRace(player)) continue;

            onTick(player);
        }
    }

    public abstract void onTick(Player player);
}
