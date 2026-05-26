package justjabka.JustRaces.Registries;

import justjabka.JustRaces.Instances.RaceInstance;
import justjabka.JustRaces.JustRacesAPI;
import justjabka.JustRaces.JustRacesRegistries;
import justjabka.JustRaces.Managers.ModifierManager;
import justjabka.JustRaces.Modifiers.Armor.*;
import justjabka.JustRaces.Modifiers.Food.GlowBerriesFoodModifier;
import justjabka.JustRaces.Modifiers.Food.MossFoodModifier;
import justjabka.JustRaces.Modifiers.Food.PhantomMembraneFoodModifier;
import justjabka.JustRaces.Modifiers.Food.SweetBerriesFoodModifier;
import justjabka.JustRaces.Modifiers.ItemModifier;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.*;

public class ModifiersRegistry {
    private static final Map<NamespacedKey, Map<Material, ItemModifier>> RACE_MODIFIERS = new HashMap<>();

    public static void register(ConfigRegistry configs) {
        registerModifiers(configs);
        bindModifiers();

        JustRacesAPI.getLogger().info("Successfully registered {} item modifiers!", JustRacesRegistries.MODIFIERS.keys().size());
    }

    private static void registerModifiers(ConfigRegistry configs) {
        // List off all unique modificator
        List<ItemModifier> modifiers = List.of(
                new LeatherArmorModifier(create("leather_armor"), configs.leatherArmorModifierConfig),
                new CopperArmorModifier(create("copper_armor"), configs.copperArmorModifierConfig),
                new ChainmailArmorModifier(create("chainmail_armor"), configs.chainmailArmorModifierConfig),
                new IronArmorModifier(create("iron_armor"), configs.ironArmorModifierConfig),
                new GoldenArmorModifier(create("golden_armor"), configs.goldenArmorModifierConfig),

                new PhantomMembraneFoodModifier(create("phantom_membrane"), configs.phantomMembraneFoodModifierConfig),
                new GlowBerriesFoodModifier(create("glow_berries"), configs.glowBerriesFoodModifierConfig),
                new MossFoodModifier(create("moss"), configs.mossFoodModifierConfig),
                new SweetBerriesFoodModifier(create("sweet_berries"), configs.sweetBerriesFoodModifierConfig)
        );

        // Register modifiers globally
        for (ItemModifier modifier : modifiers) {
            JustRacesRegistries.MODIFIERS.register(modifier.getKey(), modifier);
        }
    }

    public static void bindModifiers() {
        RACE_MODIFIERS.clear();

        Collection<RaceInstance> races = JustRacesRegistries.RACES.values();

        for (RaceInstance race : races) {
            for (NamespacedKey modifierKey : JustRacesRegistries.MODIFIERS.keys()) {
                Set<Material> materials = race.getMaterialsForModifier(modifierKey.toString());

                if (materials.isEmpty()) {
                    materials = race.getMaterialsForModifier(modifierKey.getKey());
                }

                if (materials.isEmpty()) continue;

                ItemModifier modifier = JustRacesRegistries.MODIFIERS.get(modifierKey);

                for (Material material : materials) {
                    RACE_MODIFIERS
                            .computeIfAbsent(race.getKey(), k -> new HashMap<>())
                            .put(material, modifier);
                }
            }
        }

        ModifierManager.updateRaceModifiers(RACE_MODIFIERS);
    }

    private static NamespacedKey create(String key) {
        return new NamespacedKey(JustRacesAPI.NAMESPACE, key);
    }
}
