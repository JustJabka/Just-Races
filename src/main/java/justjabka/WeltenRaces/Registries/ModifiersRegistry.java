package justjabka.WeltenRaces.Registries;

import justjabka.WeltenRaces.Instances.RaceInstance;
import justjabka.WeltenRaces.Modifiers.Contents.Armor.*;
import justjabka.WeltenRaces.Modifiers.Contents.Food.GlowBerriesFoodModifier;
import justjabka.WeltenRaces.Modifiers.Contents.Food.MossFoodModifier;
import justjabka.WeltenRaces.Modifiers.Contents.Food.PhantomMembraneFoodModifier;
import justjabka.WeltenRaces.Modifiers.Contents.Food.SweetBerriesFoodModifier;
import justjabka.WeltenRaces.Modifiers.ItemModifier;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ModifiersRegistry {
    public static final Map<NamespacedKey, Map<Material, ItemModifier>> RACE_MODIFIERS = new HashMap<>();
    public static final Map<NamespacedKey, ItemModifier> MODIFIERS_BY_KEY = new HashMap<>();

    public static void register(Plugin plugin, ConfigRegistry configs) {
        RACE_MODIFIERS.clear();
        MODIFIERS_BY_KEY.clear();

        registerModifiers(plugin, configs);
        bindModifiers();

        WeltenRaces.LOGGER.info("Successfully registered {} item modifiers!", MODIFIERS_BY_KEY.size());
    }

    private static void registerModifiers(Plugin plugin, ConfigRegistry configs) {
        // List off all unique modificator
        List<ItemModifier> allModifiers = List.of(
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
        for (ItemModifier modifier : allModifiers) {
            MODIFIERS_BY_KEY.put(modifier.getKey(), modifier);

            if (!(modifier instanceof Listener listener)) continue;
            Bukkit.getPluginManager().registerEvents(listener, plugin);
        }
    }

    private static void bindModifiers() {
        for (RaceInstance race : RacesRegistry.getRaces().values()) {

            for (NamespacedKey modifierKey : MODIFIERS_BY_KEY.keySet()) {
                Set<Material> materials = race.getMaterialsForModifier(modifierKey.toString());

                if (materials.isEmpty()) {
                    materials = race.getMaterialsForModifier(modifierKey.getKey());
                }

                if (materials.isEmpty()) continue;

                ItemModifier modifier = MODIFIERS_BY_KEY.get(modifierKey);

                for (Material material : materials) {
                    RACE_MODIFIERS
                            .computeIfAbsent(race.getKey(), k -> new HashMap<>())
                            .put(material, modifier);
                }
            }
        }
    }

    private static NamespacedKey create(String key) {
        return new NamespacedKey(WeltenRaces.NAMESPACE, key);
    }
}
