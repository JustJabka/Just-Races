package justjabka.WeltenRaces.Registries;

import justjabka.WeltenRaces.Abilities.*;
import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.Configs.Abilities.EcdysisConfig;
import justjabka.WeltenRaces.Configs.Abilities.PredatorVisionConfig;
import justjabka.WeltenRaces.Configs.ConfigWrapper;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class AbilitiesRegistry {
    private static final EcdysisConfig ecdysisConfig = new EcdysisConfig(loadAbilityConfig(WeltenRaces.INSTANCE, "ecdysis"));
    private static final PredatorVisionConfig predatorVisionConfig = new PredatorVisionConfig(loadAbilityConfig(WeltenRaces.INSTANCE, "predator-vision"));

    private static final List<BaseAbility> ABILITIES = List.of(
            new DamageInversion(),
            new Ecdysis(ecdysisConfig),
            new PredatorVision(predatorVisionConfig)
    );

    public static void register(Plugin plugin) {
        for (BaseAbility ability : ABILITIES) {
            Bukkit.getPluginManager().registerEvents(ability, plugin);
        }

        WeltenRaces.LOGGER.info("Successfully registered abilities!");
    }

    private static FileConfiguration loadAbilityConfig(Plugin plugin, String name) {
        return new ConfigWrapper(plugin, "abilities/%s.yml".formatted(name)).getConfig();
    }
}
