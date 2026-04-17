package justjabka.WeltenRaces.Types;

import justjabka.WeltenRaces.Modifiers.RaceModifier;
import justjabka.WeltenRaces.Modifiers.contents.LeatherArmorModifier;

public enum ModifierType {
    LEATHER_ARMOR(new LeatherArmorModifier());

    private final RaceModifier modifier;

    ModifierType(RaceModifier modifier) {
        this.modifier = modifier;
    }

    public RaceModifier get() {
        return modifier;
    }
}
