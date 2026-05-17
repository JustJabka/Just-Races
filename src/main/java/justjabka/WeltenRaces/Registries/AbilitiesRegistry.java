package justjabka.WeltenRaces.Registries;

import justjabka.WeltenRaces.Abilities.*;
import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.Types.Race;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AbilitiesRegistry {
    public static final Map<Race, Set<BaseAbility>> RACE_ABILITIES = new HashMap<>();

    private static void putRaceAbilities(ConfigRegistry configs) {
        final DamageInversionAbility damageInversion = new DamageInversionAbility(configs.damageInversionAbilityConfig);
        final EcdysisAbility ecdysis = new EcdysisAbility(configs.ecdysisAbilityConfig);
        final PredatorVisionAbility predatorVision = new PredatorVisionAbility(configs.predatorVisionAbilityConfig);
        final UnfoldWingsAbility unfoldWings = new UnfoldWingsAbility(configs.unfoldWingsAbilityConfig);
        final WildHuntAbility wildHunt = new WildHuntAbility(configs.wildHuntAbilityConfig);
        final WeightlessWillowSwayAbility weightlessWillowSway = new WeightlessWillowSwayAbility(configs.weightlessWillowSwayAbilityConfig);
        final CompressedSkyShardAbility compressedSkyShard = new CompressedSkyShardAbility(configs.compressedSkyShardAbilityConfig);
        final PoisonousWeaponAbility poisonousWeapon = new PoisonousWeaponAbility(configs.poisonousWeaponAbilityConfig);
        final PoisonousAreaAbility poisonousArea = new PoisonousAreaAbility(configs.poisonousAreaAbilityConfig);
        final AzaleaCamouflageAbility azaleaCamouflage = new AzaleaCamouflageAbility(configs.azaleaCamouflageAbilityConfig);
        final PoisonousSplitAbility poisonousSplit = new PoisonousSplitAbility(configs.poisonousSplitAbilityConfig);
        final SwiftSneakAbility swiftSneak = new SwiftSneakAbility(configs.swiftSneakAbilityConfig);
        final TrueFormAbility trueForm = new TrueFormAbility(configs.trueFormAbilityConfig);
        final PoisonousBiteAbility poisonousBite = new PoisonousBiteAbility(configs.poisonousBiteAbilityConfig);
        final VocalFryAbility vocalFryAbility = new VocalFryAbility(configs.vocalFryAbilityConfig);
        final GluttonyExecuteAbility gluttonyExecuteAbility = new GluttonyExecuteAbility(configs.gluttonyExecuteAbilityConfig);

        RACE_ABILITIES.put(Race.ARMAT, Set.of(damageInversion, ecdysis));
        RACE_ABILITIES.put(Race.PHANTOM, Set.of(predatorVision, unfoldWings, wildHunt));
        RACE_ABILITIES.put(Race.HUMAN, Set.of()); // Human solo verse💀
        RACE_ABILITIES.put(Race.SKYZERN, Set.of(weightlessWillowSway, compressedSkyShard));
        RACE_ABILITIES.put(Race.EPIPHYTE, Set.of(poisonousWeapon, poisonousArea, azaleaCamouflage));
        RACE_ABILITIES.put(Race.LIZARD, Set.of(poisonousSplit, swiftSneak, trueForm, poisonousBite, vocalFryAbility, gluttonyExecuteAbility));
    }

    public static void register(Plugin plugin, ConfigRegistry configs) {
        RACE_ABILITIES.clear();
        putRaceAbilities(configs);

        for (Set<BaseAbility> abilities : RACE_ABILITIES.values()) {
            for (BaseAbility ability : abilities) {
                Bukkit.getPluginManager().registerEvents(ability, plugin);
            }
        }

        WeltenRaces.LOGGER.info("Successfully registered abilities!");
    }
}
