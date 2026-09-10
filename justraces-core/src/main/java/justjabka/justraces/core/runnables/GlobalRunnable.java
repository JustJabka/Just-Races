package justjabka.justraces.core.runnables;

import justjabka.justraces.api.abilities.generic.BaseAbility;
import justjabka.justraces.api.managers.AbilityManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

public class GlobalRunnable extends BukkitRunnable {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            displayCooldowns(player);
        }
    }

    private static void displayCooldowns(Player player) {
        Set<BaseAbility> abilities = AbilityManager.getAbilitiesForPlayer(player);

        for (BaseAbility ability : abilities) {
            ability.updateCooldownBar(player);
        }
    }
}
