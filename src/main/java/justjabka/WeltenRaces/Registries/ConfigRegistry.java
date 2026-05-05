package justjabka.WeltenRaces.Registries;

import justjabka.WeltenRaces.Configs.Ability.*;
import justjabka.WeltenRaces.Configs.Race.ArmatRaceConfig;
import justjabka.WeltenRaces.Configs.Race.PhantomRaceConfig;
import justjabka.WeltenRaces.Configs.Race.SkyzernRaceConfig;
import org.bukkit.plugin.Plugin;

import static justjabka.WeltenRaces.Managers.ConfigManager.loadAbilityConfig;
import static justjabka.WeltenRaces.Managers.ConfigManager.loadRaceConfig;

public class ConfigRegistry {
    // Races
    public final ArmatRaceConfig armatRaceConfig;
    public final PhantomRaceConfig phantomRaceConfig;
    public final SkyzernRaceConfig skyzernRaceConfig;

    // Abilities
    public final DamageInversionAbilityConfig damageInversionAbilityConfig;
    public final EcdysisAbilityConfig ecdysisAbilityConfig;
    public final PredatorVisionAbilityConfig predatorVisionAbilityConfig;
    public final UnfoldWingsAbilityConfig unfoldWingsAbilityConfig;
    public final WildHuntAbilityConfig wildHuntAbilityConfig;

    public ConfigRegistry(Plugin plugin) {
        // Races
        this.armatRaceConfig = new ArmatRaceConfig(loadRaceConfig(plugin, "armat"));
        this.phantomRaceConfig = new PhantomRaceConfig(loadRaceConfig(plugin, "phantom"));
        this.skyzernRaceConfig = new SkyzernRaceConfig(loadRaceConfig(plugin, "skyzern"));

        // Abilities
        this.damageInversionAbilityConfig = new DamageInversionAbilityConfig(loadAbilityConfig(plugin, "damage-inversion"));
        this.ecdysisAbilityConfig = new EcdysisAbilityConfig(loadAbilityConfig(plugin, "ecdysis"));
        this.predatorVisionAbilityConfig = new PredatorVisionAbilityConfig(loadAbilityConfig(plugin, "predator-vision"));
        this.unfoldWingsAbilityConfig = new UnfoldWingsAbilityConfig(loadAbilityConfig(plugin, "unfold-wings"));
        this.wildHuntAbilityConfig = new WildHuntAbilityConfig(loadAbilityConfig(plugin, "wild-hunt"));
    }
}
