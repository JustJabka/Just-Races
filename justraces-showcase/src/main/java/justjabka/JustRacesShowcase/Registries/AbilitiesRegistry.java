package justjabka.JustRacesShowcase.Registries;

import justjabka.JustRacesShowcase.Abilities.*;
import justjabka.JustRaces.Abilities.Generic.BaseAbility;
import justjabka.JustRacesShowcase.JustRacesShowcase;
import justjabka.JustRaces.JustRacesRegistries;
import org.jetbrains.annotations.NotNull;

public class AbilitiesRegistry {
    private static void registerAbilities(ConfigRegistry configs) {
        registerAbility(new DamageInversionAbility(configs.damageInversionAbilityConfig));
        registerAbility(new EcdysisAbility(configs.ecdysisAbilityConfig));
        registerAbility(new PredatorVisionAbility(configs.predatorVisionAbilityConfig));
        registerAbility(new UnfoldWingsAbility(configs.unfoldWingsAbilityConfig));
        registerAbility(new WildHuntAbility(configs.wildHuntAbilityConfig));
        registerAbility(new WeightlessWillowSwayAbility(configs.weightlessWillowSwayAbilityConfig));
        registerAbility(new CompressedSkyShardAbility(configs.compressedSkyShardAbilityConfig));
        registerAbility(new PoisonousWeaponAbility(configs.poisonousWeaponAbilityConfig));
        registerAbility(new PoisonousAreaAbility(configs.poisonousAreaAbilityConfig));
        registerAbility(new AzaleaCamouflageAbility(configs.azaleaCamouflageAbilityConfig));
        registerAbility(new PoisonousSplitAbility(configs.poisonousSplitAbilityConfig));
        registerAbility(new SwiftSneakAbility(configs.swiftSneakAbilityConfig));
        registerAbility(new TrueFormAbility(configs.trueFormAbilityConfig));
        registerAbility(new PoisonousBiteAbility(configs.poisonousBiteAbilityConfig));
        registerAbility(new VocalFryAbility(configs.vocalFryAbilityConfig));
        registerAbility(new GluttonyExecuteAbility(configs.gluttonyExecuteAbilityConfig));
        registerAbility(new NoteAbility());
        registerAbility(new NoteBuffAbility());
        registerAbility(new StageAbility());
        registerAbility(new PersonalMelodyAbility());
    }

    public static void register(ConfigRegistry configs) {
        registerAbilities(configs);

        JustRacesShowcase.LOGGER.info("Successfully registered {} abilities!", JustRacesRegistries.ABILITIES.keys().size());
    }

    private static void registerAbility(@NotNull BaseAbility ability) {
        JustRacesRegistries.ABILITIES.register(ability.getKey(), ability);
    }
}
