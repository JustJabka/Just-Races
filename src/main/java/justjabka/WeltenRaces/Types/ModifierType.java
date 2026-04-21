package justjabka.WeltenRaces.Types;

import justjabka.WeltenRaces.Modifiers.RaceModifier;
import justjabka.WeltenRaces.Modifiers.contents.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public enum ModifierType {
    LEATHER_ARMOR(new LeatherArmorModifier("LEATHER_ARMOR", Attribute.MOVEMENT_SPEED, 0.01, AttributeModifier.Operation.ADD_NUMBER)),
    COPPER_ARMOR(new CopperArmorModifier("COPPER_ARMOR", Attribute.BLOCK_INTERACTION_RANGE, 0.5, AttributeModifier.Operation.ADD_NUMBER)),
    CHAINMAIL_ARMOR(new ChainmailArmorModifier("CHAINMAIL_ARMOR", Attribute.LUCK, 1, AttributeModifier.Operation.ADD_NUMBER)),
    IRON_ARMOR(new IronArmorModifier("IRON_ARMOR", Attribute.ARMOR_TOUGHNESS, 0.5, AttributeModifier.Operation.ADD_NUMBER)),
    GOLDEN_ARMOR(new GoldenArmorModifier("GOLDEN_ARMOR", Attribute.MAX_ABSORPTION, 1, AttributeModifier.Operation.ADD_NUMBER));

    private final RaceModifier modifier;

    ModifierType(RaceModifier modifier) {
        this.modifier = modifier;
    }

    public RaceModifier get() {
        return modifier;
    }
}
