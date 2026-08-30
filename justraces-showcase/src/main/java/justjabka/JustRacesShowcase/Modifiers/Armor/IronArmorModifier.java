package justjabka.JustRacesShowcase.Modifiers.Armor;

import justjabka.JustRaces.Modifiers.Generic.BaseArmorModifier;
import justjabka.JustRacesShowcase.JustRacesShowcase;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public class IronArmorModifier extends BaseArmorModifier {

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "armor/iron");
    }

    @Override
    public Attribute getAttribute() {
        return Attribute.ARMOR_TOUGHNESS;
    }

    @Override
    public double getAttributeAmount() {
        return getConfigDouble("attribute_amount");
    }

    @Override
    public AttributeModifier.Operation getAttributeOperation() {
        return AttributeModifier.Operation.ADD_NUMBER;
    }
}
