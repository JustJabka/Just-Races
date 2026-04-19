package justjabka.WeltenRaces.Modifiers.contents;

import justjabka.WeltenRaces.Modifiers.contents.Generic.BaseArmorModifier;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public class IronArmorModifier extends BaseArmorModifier {
    public IronArmorModifier(
            String id,
            Attribute attribute,
            double amount,
            AttributeModifier.Operation operation
    ) {
        super(id, attribute, amount, operation);
    }
}