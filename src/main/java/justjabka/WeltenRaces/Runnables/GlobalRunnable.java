package justjabka.WeltenRaces.Runnables;

import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.Instances.RaceInstance;
import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.Managers.RaceManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class GlobalRunnable extends BukkitRunnable {
    private static final String separator = "|";
    private static final TextColor separatorColor = NamedTextColor.GRAY;

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateActionBar(player);
        }
    }

    private static void updateActionBar(Player player) {
        RaceInstance playerRace = RaceManager.getRace(player);

        Set<BaseAbility> abilities = AbilityManager.getAbilitiesForRace(playerRace);
        if (abilities.isEmpty()) return;

        List<Component> displays = abilities.stream()
                .sorted(Comparator.comparing(ability -> ability.getClass().getSimpleName()))
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
