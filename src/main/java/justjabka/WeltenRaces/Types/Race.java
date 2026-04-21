package justjabka.WeltenRaces.Types;

import org.bukkit.Material;

import java.util.Map;

public enum Race {
    ARMAT(Map.ofEntries(
            Map.entry(Material.LEATHER_HELMET, ModifierType.LEATHER_ARMOR),
            Map.entry(Material.LEATHER_CHESTPLATE, ModifierType.LEATHER_ARMOR),
            Map.entry(Material.LEATHER_LEGGINGS, ModifierType.LEATHER_ARMOR),
            Map.entry(Material.LEATHER_BOOTS, ModifierType.LEATHER_ARMOR),
            Map.entry(Material.COPPER_HELMET, ModifierType.COPPER_ARMOR),
            Map.entry(Material.COPPER_CHESTPLATE, ModifierType.COPPER_ARMOR),
            Map.entry(Material.COPPER_LEGGINGS, ModifierType.COPPER_ARMOR),
            Map.entry(Material.COPPER_BOOTS, ModifierType.COPPER_ARMOR),
            Map.entry(Material.CHAINMAIL_HELMET, ModifierType.CHAINMAIL_ARMOR),
            Map.entry(Material.CHAINMAIL_CHESTPLATE, ModifierType.CHAINMAIL_ARMOR),
            Map.entry(Material.CHAINMAIL_LEGGINGS, ModifierType.CHAINMAIL_ARMOR),
            Map.entry(Material.CHAINMAIL_BOOTS, ModifierType.CHAINMAIL_ARMOR),
            Map.entry(Material.IRON_HELMET, ModifierType.IRON_ARMOR),
            Map.entry(Material.IRON_CHESTPLATE, ModifierType.IRON_ARMOR),
            Map.entry(Material.IRON_LEGGINGS, ModifierType.IRON_ARMOR),
            Map.entry(Material.IRON_BOOTS, ModifierType.IRON_ARMOR),
            Map.entry(Material.GOLDEN_HELMET, ModifierType.GOLDEN_ARMOR),
            Map.entry(Material.GOLDEN_CHESTPLATE, ModifierType.GOLDEN_ARMOR),
            Map.entry(Material.GOLDEN_LEGGINGS, ModifierType.GOLDEN_ARMOR),
            Map.entry(Material.GOLDEN_BOOTS, ModifierType.GOLDEN_ARMOR)
    )),
    EPIPHYTE(Map.of()),
    HUMAN(Map.of()),
    LIZARD(Map.of()),
    PHANTOM(Map.of()),
    SKYZERN(Map.of());

    private final Map<Material, ModifierType> itemModifiers;

    Race(Map<Material, ModifierType> itemModifiers) {
        this.itemModifiers = itemModifiers;
    }

    public ModifierType getModifierFor(Material material) {
        return itemModifiers.get(material);
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}