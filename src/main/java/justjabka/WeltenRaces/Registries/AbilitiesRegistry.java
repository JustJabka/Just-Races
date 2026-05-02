package justjabka.WeltenRaces.Registries;

import justjabka.WeltenRaces.Abilities.*;
import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.Configs.Abilities.EcdysisConfig;
import justjabka.WeltenRaces.Configs.Abilities.PredatorVisionConfig;
import justjabka.WeltenRaces.Configs.Abilities.UnfoldWingsConfig;
import justjabka.WeltenRaces.Configs.Abilities.WildHuntConfig;
import justjabka.WeltenRaces.Configs.ConfigWrapper;
import justjabka.WeltenRaces.Types.Race;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;

public class AbilitiesRegistry {
    // Configs
    private static final EcdysisConfig ECDYSIS_CONFIG = new EcdysisConfig(loadAbilityConfig(WeltenRaces.INSTANCE, "ecdysis"));
    private static final PredatorVisionConfig PREDATOR_VISION_CONFIG = new PredatorVisionConfig(loadAbilityConfig(WeltenRaces.INSTANCE, "predator-vision"));
    private static final UnfoldWingsConfig UNFOLD_WINGS_CONFIG = new UnfoldWingsConfig(loadAbilityConfig(WeltenRaces.INSTANCE, "unfold-wings"));
    private static final WildHuntConfig WILD_HUNT_CONFIG = new WildHuntConfig(loadAbilityConfig(WeltenRaces.INSTANCE, "wild-hunt"));

    // Abilities
    private static final DamageInversion DAMAGE_INVERSION = new DamageInversion();
    private static final Ecdysis ECDYSIS = new Ecdysis(ECDYSIS_CONFIG);
    private static final PredatorVision PREDATOR_VISION = new PredatorVision(PREDATOR_VISION_CONFIG);
    private static final UnfoldWings UNFOLD_WINGS = new UnfoldWings(UNFOLD_WINGS_CONFIG);
    private static final WildHunt WILD_HUNT = new WildHunt(WILD_HUNT_CONFIG);

    public static final Map<Race, List<BaseAbility>> RACE_ABILITIES = Map.of(
            Race.ARMAT, List.of(DAMAGE_INVERSION, ECDYSIS),
            Race.PHANTOM, List.of(PREDATOR_VISION, UNFOLD_WINGS, WILD_HUNT),
            Race.HUMAN, List.of() // Human solo verse💀
    );

    public static void register(Plugin plugin) {
        for (List<BaseAbility> abilities : RACE_ABILITIES.values()) {
            for (BaseAbility ability : abilities) {
                Bukkit.getPluginManager().registerEvents(ability, plugin);
            }
        }

        WeltenRaces.LOGGER.info("Successfully registered abilities!");
    }

    private static FileConfiguration loadAbilityConfig(Plugin plugin, String name) {
        return new ConfigWrapper(plugin, "abilities/%s.yml".formatted(name)).getConfig();
    }
}
