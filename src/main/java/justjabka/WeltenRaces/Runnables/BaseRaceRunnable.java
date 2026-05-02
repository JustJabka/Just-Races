package justjabka.WeltenRaces.Runnables;

import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.scheduler.BukkitRunnable;

public class BaseRaceRunnable extends BukkitRunnable {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PersistentDataContainer abilities = AbilityManager.getAbilities(player);

            if (abilities.isEmpty()) continue;

            WeltenRaces.LOGGER.info("Updated actionbar!");
        }
    }
}
