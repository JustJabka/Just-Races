package justjabka.WeltenRaces.Modifiers.Contents;

import justjabka.WeltenRaces.Modifiers.Contents.Generic.BaseArmorModifier;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public class GoldenArmorModifier extends BaseArmorModifier {
    public GoldenArmorModifier(
            String id,
            Attribute attribute,
            double amount,
            AttributeModifier.Operation operation
    ) {
        super(id, attribute, amount, operation);
    }
}