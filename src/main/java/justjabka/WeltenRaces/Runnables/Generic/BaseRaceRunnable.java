package justjabka.WeltenRaces.Runnables.Generic;

import justjabka.WeltenRaces.DataProvider.RaceProvider;
import justjabka.WeltenRaces.Managers.RaceManager;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.spongepowered.configurate.CommentedConfigurationNode;

public abstract class BaseRaceRunnable extends BukkitRunnable {
    public abstract NamespacedKey getRaceKey();

    public CommentedConfigurationNode getConfig() {
        return RaceProvider.get(getRaceKey()).getConfig();
    }

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
