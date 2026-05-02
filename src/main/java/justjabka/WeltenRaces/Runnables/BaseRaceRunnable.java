package justjabka.WeltenRaces.Runnables;

import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.Types.Race;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class BaseRaceRunnable extends BukkitRunnable {
    private static final String separator = "|";
    private static final TextColor separatorColor = NamedTextColor.GRAY;

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateActionBar(player);
        }
    }

    private static void updateActionBar(Player player) {
        Race playerRace = RaceManager.getRace(player);

        List<BaseAbility> abilities = AbilityManager.getAbilitiesForRace(playerRace);
        if (abilities.isEmpty()) return;

        Component message = Component.empty();

        for (int i = 0; i < abilities.size(); i++) {
            Component abilityDisplay = abilities.get(i).getAbilityDisplay(player);

            message = message.append(abilityDisplay);

            if (i > abilities.size() - 2) continue;
            message = message.append(Component.text(" %s ".formatted(separator))).color(separatorColor);
        }

        player.sendActionBar(message);
    }
}
