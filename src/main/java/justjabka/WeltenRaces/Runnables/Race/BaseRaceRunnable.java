package justjabka.WeltenRaces.Runnables.Race;

import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.Types.Race;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.function.Predicate;

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

        List<Component> displays = abilities.stream()
                .map(ability -> ability.getAbilityDisplay(player))
                .filter(Predicate.not(component -> component.equals(Component.empty())))
                .toList();

        Component message = Component.join(
                JoinConfiguration.separator(Component.text(" %s ".formatted(separator), separatorColor)),
                displays
        );

        player.sendActionBar(message);
    }
}
