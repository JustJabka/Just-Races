package justjabka.WeltenRaces.Registries;

import justjabka.WeltenRaces.Abilities.*;
import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.Types.Race;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbilitiesRegistry {
    public static final Map<Race, List<BaseAbility>> RACE_ABILITIES = new HashMap<>();

    private static void putRaceAbilities(ConfigRegistry configs) {
        final DamageInversionAbility damageInversion = new DamageInversionAbility(configs.damageInversionAbilityConfig);
        final EcdysisAbility ecdysis = new EcdysisAbility(configs.ecdysisAbilityConfig);
        final PredatorVisionAbility predatorVision = new PredatorVisionAbility(configs.predatorVisionAbilityConfig);
        final UnfoldWingsAbility unfoldWings = new UnfoldWingsAbility(configs.unfoldWingsAbilityConfig);
        final WildHuntAbility wildHunt = new WildHuntAbility(configs.wildHuntAbilityConfig);
        final WeightlessWillowSwayAbility weightlessWillowSway = new WeightlessWillowSwayAbility(configs.weightlessWillowSwayAbilityConfig);
        final CompressedSkyShardAbility compressedSkyShard = new CompressedSkyShardAbility(configs.compressedSkyShardAbilityConfig);

        RACE_ABILITIES.put(Race.ARMAT, List.of(damageInversion, ecdysis));
        RACE_ABILITIES.put(Race.PHANTOM, List.of(predatorVision, unfoldWings, wildHunt));
        RACE_ABILITIES.put(Race.HUMAN, List.of()); // Human solo verse💀
        RACE_ABILITIES.put(Race.SKYZERN, List.of(weightlessWillowSway, compressedSkyShard));
    }

    public static void register(Plugin plugin, ConfigRegistry configs) {
        RACE_ABILITIES.clear();
        putRaceAbilities(configs);

        for (List<BaseAbility> abilities : RACE_ABILITIES.values()) {
            for (BaseAbility ability : abilities) {
                Bukkit.getPluginManager().registerEvents(ability, plugin);
            }
        }

        WeltenRaces.LOGGER.info("Successfully registered abilities!");
    }
}
