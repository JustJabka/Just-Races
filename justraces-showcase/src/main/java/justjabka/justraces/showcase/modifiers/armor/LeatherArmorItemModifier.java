package justjabka.justraces.showcase.modifiers.armor;

import justjabka.justraces.api.itemmodifiers.generic.ConfigurableItemModifier;
import justjabka.justraces.api.itemmodifiers.generic.BaseArmorItemModifier;
import justjabka.justraces.showcase.JustRacesShowcase;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public class LeatherArmorItemModifier extends BaseArmorItemModifier implements ConfigurableItemModifier {

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "armor/leather");
    }

    @Override
    public Attribute getAttribute() {
        return Attribute.MOVEMENT_SPEED;
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
