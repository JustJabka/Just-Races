package justjabka.WeltenRaces.Registries;

import justjabka.WeltenRaces.Abilities.*;
import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class AbilitiesRegistry {
    private static final Map<NamespacedKey, BaseAbility> ABILITIES = new HashMap<>();

    private static void registerAllAbilities(ConfigRegistry configs) {
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

    private static void registerAbility(BaseAbility ability) {
        ABILITIES.put(ability.getKey(), ability);
    }

    public static void register(Plugin plugin, ConfigRegistry configs) {
        ABILITIES.clear();
        registerAllAbilities(configs);

        for (BaseAbility ability : ABILITIES.values()) {
            Bukkit.getPluginManager().registerEvents(ability, plugin);
        }

        WeltenRaces.LOGGER.info("Successfully registered {} abilities!", ABILITIES.size());
    }

    public static Map<NamespacedKey, BaseAbility> getAbilities() {
        return ABILITIES;
    }
}
