package justjabka.WeltenRaces.Modifiers.Contents.Armor;

import justjabka.WeltenRaces.Modifiers.Contents.Generic.BaseArmorModifier;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public class IronArmorModifier extends BaseArmorModifier {
    public IronArmorModifier(String id) {
        super(
                id,
                Attribute.ARMOR_TOUGHNESS,
                0.5,
                AttributeModifier.Operation.ADD_NUMBER
        );
    }
}
