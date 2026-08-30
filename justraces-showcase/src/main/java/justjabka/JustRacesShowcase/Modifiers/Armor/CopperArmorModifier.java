package justjabka.JustRacesShowcase.Modifiers.Armor;

import justjabka.JustRaces.Interfaces.Configurable.ItemModifierConfigurable;
import justjabka.JustRaces.Modifiers.Generic.BaseArmorModifier;
import justjabka.JustRacesShowcase.JustRacesShowcase;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public class CopperArmorModifier extends BaseArmorModifier implements ItemModifierConfigurable {
    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "armor/copper");
    }

    @Override
    public Attribute getAttribute() {
        return Attribute.BLOCK_INTERACTION_RANGE;
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
