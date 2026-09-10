package justjabka.justraces.showcase.modifiers.armor;

import justjabka.justraces.api.interfaces.configurable.ItemModifierConfigurable;
import justjabka.justraces.api.modifiers.generic.BaseArmorModifier;
import justjabka.justraces.showcase.JustRacesShowcase;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public class GoldenArmorModifier extends BaseArmorModifier implements ItemModifierConfigurable {

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "armor/golden");
    }
    @Override
    public Attribute getAttribute() {
        return Attribute.MAX_ABSORPTION;
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
