package justjabka.JustRacesShowcase.Modifiers.Armor;

import justjabka.JustRaces.Modifiers.Generic.BaseArmorModifier;
import justjabka.JustRacesShowcase.JustRacesShowcase;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public class GoldenArmorModifier extends BaseArmorModifier {

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "golden_armor");
    }
    @Override
    public Attribute getAttribute() {
        return Attribute.MAX_ABSORPTION;
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
