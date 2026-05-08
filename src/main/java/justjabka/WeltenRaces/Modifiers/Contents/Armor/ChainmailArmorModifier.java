package justjabka.WeltenRaces.Modifiers.Contents.Armor;

import justjabka.WeltenRaces.Modifiers.Contents.Generic.BaseArmorModifier;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public class ChainmailArmorModifier extends BaseArmorModifier {
    public ChainmailArmorModifier(String id) {
        super(
                id,
                Attribute.LUCK,
                1,
                AttributeModifier.Operation.ADD_NUMBER
        );
    }
}
