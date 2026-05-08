package justjabka.WeltenRaces.Registries;

import justjabka.WeltenRaces.Modifiers.Contents.Armor.*;
import justjabka.WeltenRaces.Modifiers.Contents.Food.GlowBerriesFoodModifier;
import justjabka.WeltenRaces.Modifiers.Contents.Food.MossFoodModifier;
import justjabka.WeltenRaces.Modifiers.Contents.Food.PhantomMembraneFoodModifier;
import justjabka.WeltenRaces.Modifiers.ItemModifier;
import justjabka.WeltenRaces.Types.Race;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class ModifierRegistry {
    public static final Map<Race, Map<Material, ItemModifier>> RACE_MODIFIERS = new HashMap<>();
    public static final Map<String, ItemModifier> MODIFIERS_BY_ID = new HashMap<>();

    public static void register(ConfigRegistry configs) {
        RACE_MODIFIERS.clear();
        MODIFIERS_BY_ID.clear();

        registerModifiers(configs);

        WeltenRaces.LOGGER.info("Successfully registered item modifiers!");
    }

    private static void registerModifiers(ConfigRegistry configs) {
        // Armor
        ItemModifier leatherArmor = new LeatherArmorModifier("leather_armor");
        ItemModifier copperArmor = new CopperArmorModifier("copper_armor");
        ItemModifier chainmailArmor = new ChainmailArmorModifier("chainmail_armor");
        ItemModifier ironArmor = new IronArmorModifier("iron_armor");
        ItemModifier goldenArmor = new GoldenArmorModifier("golden_armor");

        // Food
        ItemModifier phantomMembrane = new PhantomMembraneFoodModifier("phantom_membrane");

        ItemModifier glowBerries = new GlowBerriesFoodModifier("glow_berries");
        ItemModifier moss = new MossFoodModifier("moss");

        registerModifier(Race.ARMAT, Material.LEATHER_HELMET, leatherArmor);
        registerModifier(Race.ARMAT, Material.LEATHER_CHESTPLATE, leatherArmor);
        registerModifier(Race.ARMAT, Material.LEATHER_LEGGINGS, leatherArmor);
        registerModifier(Race.ARMAT, Material.LEATHER_BOOTS, leatherArmor);
        registerModifier(Race.ARMAT, Material.COPPER_HELMET, copperArmor);
        registerModifier(Race.ARMAT, Material.COPPER_CHESTPLATE, copperArmor);
        registerModifier(Race.ARMAT, Material.COPPER_LEGGINGS, copperArmor);
        registerModifier(Race.ARMAT, Material.COPPER_BOOTS, copperArmor);
        registerModifier(Race.ARMAT, Material.CHAINMAIL_HELMET, chainmailArmor);
        registerModifier(Race.ARMAT, Material.CHAINMAIL_CHESTPLATE, chainmailArmor);
        registerModifier(Race.ARMAT, Material.CHAINMAIL_LEGGINGS, chainmailArmor);
        registerModifier(Race.ARMAT, Material.CHAINMAIL_BOOTS, chainmailArmor);
        registerModifier(Race.ARMAT, Material.IRON_HELMET, ironArmor);
        registerModifier(Race.ARMAT, Material.IRON_CHESTPLATE, ironArmor);
        registerModifier(Race.ARMAT, Material.IRON_LEGGINGS, ironArmor);
        registerModifier(Race.ARMAT, Material.IRON_BOOTS, ironArmor);
        registerModifier(Race.ARMAT, Material.GOLDEN_HELMET, goldenArmor);
        registerModifier(Race.ARMAT, Material.GOLDEN_CHESTPLATE, goldenArmor);
        registerModifier(Race.ARMAT, Material.GOLDEN_LEGGINGS, goldenArmor);
        registerModifier(Race.ARMAT, Material.GOLDEN_BOOTS, goldenArmor);

        registerModifier(Race.PHANTOM, Material.PHANTOM_MEMBRANE, phantomMembrane);

        registerModifier(Race.EPIPHYTE, Material.GLOW_BERRIES, glowBerries);
        registerModifier(Race.EPIPHYTE, Material.MOSS_BLOCK, moss);
    }

    private static void registerModifier(Race race, Material material, ItemModifier modifier) {
        MODIFIERS_BY_ID.put(modifier.toString(), modifier);
        RACE_MODIFIERS.computeIfAbsent(race, k -> new HashMap<>()).put(material, modifier);
    }

    public static ItemModifier getById(String id) {
        return MODIFIERS_BY_ID.get(id);
    }
}
