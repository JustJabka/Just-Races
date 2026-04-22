package justjabka.WeltenRaces.Modifiers.Contents;

import justjabka.WeltenRaces.Modifiers.Contents.Generic.BaseArmorModifier;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public class ChainmailArmorModifier extends BaseArmorModifier {
    public ChainmailArmorModifier(
            String id,
            Attribute attribute,
            double amount,
            AttributeModifier.Operation operation
    ) {
        super(id, attribute, amount, operation);
    }
}