package justjabka.JustRaces.Runnables;

import justjabka.JustRaces.Abilities.Generic.BaseAbility;
import justjabka.JustRaces.Instances.RaceInstance;
import justjabka.JustRaces.Managers.AbilityManager;
import justjabka.JustRaces.Managers.RaceManager;
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
        RaceInstance playerRace = RaceManager.getRace(player);
        Set<BaseAbility> abilities = AbilityManager.getAbilitiesForRace(playerRace);

        for (BaseAbility ability : abilities) {
            ability.updateCooldownBar(player);
        }
    }
}
