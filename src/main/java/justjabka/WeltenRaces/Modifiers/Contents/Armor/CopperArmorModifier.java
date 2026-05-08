package justjabka.WeltenRaces.Modifiers.Contents.Armor;

import justjabka.WeltenRaces.Modifiers.Contents.Generic.BaseArmorModifier;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public class CopperArmorModifier extends BaseArmorModifier {
    public CopperArmorModifier(String id) {
        super(
                id,
                Attribute.BLOCK_INTERACTION_RANGE,
                0.5,
                AttributeModifier.Operation.ADD_NUMBER
        );
    }
}
