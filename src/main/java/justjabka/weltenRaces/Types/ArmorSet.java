package justjabka.weltenRaces.Types;

public enum ArmorSet {
    NONE,
    LEATHER,
    COPPER,
    IRON,
    GOLDEN,
    DIAMOND,
    NETHERITE;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
