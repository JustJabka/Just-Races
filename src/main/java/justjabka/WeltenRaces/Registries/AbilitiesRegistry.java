package justjabka.WeltenRaces.Registries;

import justjabka.WeltenRaces.Abilities.*;
import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AbilitiesRegistry {
    private static final Map<NamespacedKey, BaseAbility> ABILITIES = new HashMap<>();

    public static void registerAbility(@NotNull Plugin plugin, @NotNull BaseAbility ability) {
        if (ABILITIES.containsKey(ability.getKey())) {
            WeltenRaces.LOGGER.warn("Ability {} is already registered! Overriding...", ability.getKey());
        }

        ABILITIES.put(ability.getKey(), ability);
        Bukkit.getPluginManager().registerEvents(ability, plugin);
    }

    private static void registerAbilities(Plugin plugin, ConfigRegistry configs) {
        registerAbility(plugin, new DamageInversionAbility(configs.damageInversionAbilityConfig));
        registerAbility(plugin, new EcdysisAbility(configs.ecdysisAbilityConfig));
        registerAbility(plugin, new PredatorVisionAbility(configs.predatorVisionAbilityConfig));
        registerAbility(plugin, new UnfoldWingsAbility(configs.unfoldWingsAbilityConfig));
        registerAbility(plugin, new WildHuntAbility(configs.wildHuntAbilityConfig));
        registerAbility(plugin, new WeightlessWillowSwayAbility(configs.weightlessWillowSwayAbilityConfig));
        registerAbility(plugin, new CompressedSkyShardAbility(configs.compressedSkyShardAbilityConfig));
        registerAbility(plugin, new PoisonousWeaponAbility(configs.poisonousWeaponAbilityConfig));
        registerAbility(plugin, new PoisonousAreaAbility(configs.poisonousAreaAbilityConfig));
        registerAbility(plugin, new AzaleaCamouflageAbility(configs.azaleaCamouflageAbilityConfig));
        registerAbility(plugin, new PoisonousSplitAbility(configs.poisonousSplitAbilityConfig));
        registerAbility(plugin, new SwiftSneakAbility(configs.swiftSneakAbilityConfig));
        registerAbility(plugin, new TrueFormAbility(configs.trueFormAbilityConfig));
        registerAbility(plugin, new PoisonousBiteAbility(configs.poisonousBiteAbilityConfig));
        registerAbility(plugin, new VocalFryAbility(configs.vocalFryAbilityConfig));
        registerAbility(plugin, new GluttonyExecuteAbility(configs.gluttonyExecuteAbilityConfig));
        registerAbility(plugin, new NoteAbility());
        registerAbility(plugin, new NoteBuffAbility());
        registerAbility(plugin, new StageAbility());
        registerAbility(plugin, new PersonalMelodyAbility());
    }

    public static void register(Plugin plugin, ConfigRegistry configs) {
        registerAbilities(plugin, configs);

        WeltenRaces.LOGGER.info("Successfully registered {} abilities!", ABILITIES.size());
    }

    public static Map<NamespacedKey, BaseAbility> getAbilities() {
        return Collections.unmodifiableMap(ABILITIES);
    }
}
