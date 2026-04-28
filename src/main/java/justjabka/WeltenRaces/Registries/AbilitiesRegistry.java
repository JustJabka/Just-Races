package justjabka.WeltenRaces.Registries;

import justjabka.WeltenRaces.Abilities.BaseAbility;
import justjabka.WeltenRaces.Abilities.DamageInversion;
import justjabka.WeltenRaces.Abilities.Ecdysis;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class AbilitiesRegistry {
    private static final List<BaseAbility> abilities = List.of(
            new DamageInversion(),
            new Ecdysis()
    );

    public static void register(Plugin plugin) {
        for (BaseAbility ability : abilities) {
            Bukkit.getPluginManager().registerEvents(ability, plugin);
        }

        WeltenRaces.LOGGER.info("Successfully registered abilities!");
    }
}
