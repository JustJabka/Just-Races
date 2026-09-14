package justjabka.justraces.api.traits.generic;

import justjabka.justraces.api.common.PlayerTickRunnable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class BaseTraitRunnable extends BukkitRunnable implements PlayerTickRunnable, Trait {

    public abstract long getTickPeriod();

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!isRequiredTrait(player)) continue;

            onTick(player);
        }
    }
}
