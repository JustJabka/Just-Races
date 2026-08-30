package justjabka.JustRacesShowcase.Modifiers.Armor;

import justjabka.JustRaces.Modifiers.Generic.BaseArmorModifier;
import justjabka.JustRacesShowcase.JustRacesShowcase;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public class ChainmailArmorModifier extends BaseArmorModifier {

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "chainmail_armor");
    }

    @Override
    public Attribute getAttribute() {
        return Attribute.LUCK;
    }

    @Override
    public double getAttributeAmount() {
        return 0;
    }

    @Override
    public AttributeModifier.Operation getAttributeOperation() {
        return AttributeModifier.Operation.ADD_NUMBER;
    }
}
