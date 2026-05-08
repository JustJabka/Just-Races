package justjabka.WeltenRaces.Modifiers.Contents.Armor;

import justjabka.WeltenRaces.Modifiers.Contents.Generic.BaseArmorModifier;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public class GoldenArmorModifier extends BaseArmorModifier {
    public GoldenArmorModifier(String id) {
        super(
                id,
                Attribute.MAX_ABSORPTION,
                1,
                AttributeModifier.Operation.ADD_NUMBER
        );
    }
}
