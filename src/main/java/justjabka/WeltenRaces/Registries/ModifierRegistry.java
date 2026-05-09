package justjabka.WeltenRaces.Registries;

import justjabka.WeltenRaces.Modifiers.Contents.Armor.*;
import justjabka.WeltenRaces.Modifiers.Contents.Food.GlowBerriesFoodModifier;
import justjabka.WeltenRaces.Modifiers.Contents.Food.MossFoodModifier;
import justjabka.WeltenRaces.Modifiers.Contents.Food.PhantomMembraneFoodModifier;
import justjabka.WeltenRaces.Modifiers.ItemModifier;
import justjabka.WeltenRaces.Types.Race;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModifierRegistry {
    public static final Map<Race, Map<Material, ItemModifier>> RACE_MODIFIERS = new HashMap<>();
    public static final Map<String, ItemModifier> MODIFIERS_BY_ID = new HashMap<>();

    public static void register(Plugin plugin, ConfigRegistry configs) {
        RACE_MODIFIERS.clear();
        MODIFIERS_BY_ID.clear();

        registerModifiers(plugin, configs);
        bindModifiers();

        WeltenRaces.LOGGER.info("Successfully registered item modifiers!");
    }

    private static void registerModifiers(Plugin plugin, ConfigRegistry configs) {
        // List off all unique modificator
        List<ItemModifier> allModifiers = List.of(
                new LeatherArmorModifier("leather_armor", configs.leatherArmorModifierConfig),
                new CopperArmorModifier("copper_armor", configs.copperArmorModifierConfig),
                new ChainmailArmorModifier("chainmail_armor", configs.chainmailArmorModifierConfig),
                new IronArmorModifier("iron_armor", configs.ironArmorModifierConfig),
                new GoldenArmorModifier("golden_armor", configs.goldenArmorModifierConfig),

                new PhantomMembraneFoodModifier("phantom_membrane", configs.phantomMembraneFoodModifierConfig),
                new GlowBerriesFoodModifier("glow_berries", configs.glowBerriesFoodModifierConfig),
                new MossFoodModifier("moss", configs.mossFoodModifierConfig)
        );

        // Register modifiers globally
        for (ItemModifier modifier : allModifiers) {
            MODIFIERS_BY_ID.put(modifier.getId(), modifier);

            if (modifier instanceof Listener listener) {
                Bukkit.getPluginManager().registerEvents(listener, plugin);
            }
        }
    }

    private static void bindModifiers() {
        // Armor
        ItemModifier leatherArmor = MODIFIERS_BY_ID.get("leather_armor");
        ItemModifier copperArmor = MODIFIERS_BY_ID.get("copper_armor");
        ItemModifier chainmailArmor = MODIFIERS_BY_ID.get("chainmail_armor");
        ItemModifier ironArmor = MODIFIERS_BY_ID.get("iron_armor");
        ItemModifier goldenArmor = MODIFIERS_BY_ID.get("golden_armor");

        // Food
        ItemModifier phantomMembrane = MODIFIERS_BY_ID.get("phantom_membrane");

        ItemModifier glowBerries = MODIFIERS_BY_ID.get("glow_berries");
        ItemModifier moss = MODIFIERS_BY_ID.get("moss");

        bindModifier(Race.ARMAT, Material.LEATHER_HELMET, leatherArmor);
        bindModifier(Race.ARMAT, Material.LEATHER_CHESTPLATE, leatherArmor);
        bindModifier(Race.ARMAT, Material.LEATHER_LEGGINGS, leatherArmor);
        bindModifier(Race.ARMAT, Material.LEATHER_BOOTS, leatherArmor);
        bindModifier(Race.ARMAT, Material.COPPER_HELMET, copperArmor);
        bindModifier(Race.ARMAT, Material.COPPER_CHESTPLATE, copperArmor);
        bindModifier(Race.ARMAT, Material.COPPER_LEGGINGS, copperArmor);
        bindModifier(Race.ARMAT, Material.COPPER_BOOTS, copperArmor);
        bindModifier(Race.ARMAT, Material.CHAINMAIL_HELMET, chainmailArmor);
        bindModifier(Race.ARMAT, Material.CHAINMAIL_CHESTPLATE, chainmailArmor);
        bindModifier(Race.ARMAT, Material.CHAINMAIL_LEGGINGS, chainmailArmor);
        bindModifier(Race.ARMAT, Material.CHAINMAIL_BOOTS, chainmailArmor);
        bindModifier(Race.ARMAT, Material.IRON_HELMET, ironArmor);
        bindModifier(Race.ARMAT, Material.IRON_CHESTPLATE, ironArmor);
        bindModifier(Race.ARMAT, Material.IRON_LEGGINGS, ironArmor);
        bindModifier(Race.ARMAT, Material.IRON_BOOTS, ironArmor);
        bindModifier(Race.ARMAT, Material.GOLDEN_HELMET, goldenArmor);
        bindModifier(Race.ARMAT, Material.GOLDEN_CHESTPLATE, goldenArmor);
        bindModifier(Race.ARMAT, Material.GOLDEN_LEGGINGS, goldenArmor);
        bindModifier(Race.ARMAT, Material.GOLDEN_BOOTS, goldenArmor);

        bindModifier(Race.PHANTOM, Material.PHANTOM_MEMBRANE, phantomMembrane);

        bindModifier(Race.EPIPHYTE, Material.GLOW_BERRIES, glowBerries);
        bindModifier(Race.EPIPHYTE, Material.MOSS_BLOCK, moss);
    }

    /**
     * @param race the race to which the modifier is bound
     * @param material the material to be modified for this race
     * @param modifier modifier that will be applied
     */
    private static void bindModifier(Race race, Material material, ItemModifier modifier) {
        RACE_MODIFIERS.computeIfAbsent(race, k -> new HashMap<>()).put(material, modifier);
    }
}
