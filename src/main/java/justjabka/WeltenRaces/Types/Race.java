package justjabka.WeltenRaces.Types;

import org.bukkit.Material;

import java.util.Map;

public enum Race {
    ARMAT(Map.of(
            Material.LEATHER_HELMET, ModifierType.LEATHER_ARMOR,
            Material.LEATHER_CHESTPLATE, ModifierType.LEATHER_ARMOR,
            Material.LEATHER_LEGGINGS, ModifierType.LEATHER_ARMOR,
            Material.LEATHER_BOOTS, ModifierType.LEATHER_ARMOR
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