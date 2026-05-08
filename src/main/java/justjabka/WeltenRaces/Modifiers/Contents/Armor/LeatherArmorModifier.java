package justjabka.WeltenRaces.Modifiers.Contents.Armor;

import justjabka.WeltenRaces.Modifiers.Contents.Generic.BaseArmorModifier;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public class LeatherArmorModifier extends BaseArmorModifier {
    public LeatherArmorModifier(String id) {
        super(
                id,
                Attribute.MOVEMENT_SPEED,
                0.01,
                AttributeModifier.Operation.ADD_NUMBER
        );
    }
}
