package justjabka.WeltenRaces.Types;

public enum ArmorSet {
    NONE,
    LEATHER,
    COPPER,
    CHAINMAIL,
    IRON,
    GOLDEN,
    DIAMOND,
    NETHERITE;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

    public static ArmorSet fromMaterialName(String name) {
        if (name.startsWith("leather_")) return LEATHER;
        if (name.startsWith("copper_")) return COPPER;
        if (name.startsWith("chainmail_")) return CHAINMAIL;
        if (name.startsWith("iron_")) return IRON;
        if (name.startsWith("golden_")) return GOLDEN;
        if (name.startsWith("diamond_")) return DIAMOND;
        if (name.startsWith("netherite_")) return NETHERITE;
        return NONE;
    }
}