package justjabka.JustRaces.Types;

public enum Trigger {
    LEFT_CLICK,
    RIGHT_CLICK,

    SNEAK_ON,
    SNEAK_OFF,
    SNEAK_TOGGLE,

    JUMP,
    OFFHAND_SWAP,
    RIGHT_CLICK_CHESTPLATE,

    CUSTOM;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
