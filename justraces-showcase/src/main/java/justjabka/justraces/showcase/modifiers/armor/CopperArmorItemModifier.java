package justjabka.justraces.showcase.modifiers.armor;

import justjabka.justraces.api.itemmodifiers.generic.BaseArmorItemModifier;
import justjabka.justraces.api.itemmodifiers.generic.ConfigurableItemModifier;
import justjabka.justraces.showcase.JustRacesShowcase;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public class CopperArmorItemModifier extends BaseArmorItemModifier implements ConfigurableItemModifier {
    @Override
    public @NonNull NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "armor/copper");
    }

    @Override
    public @Nullable UnkeyedAttributeModifier attributeModifier() {
        return new UnkeyedAttributeModifier(
                Attribute.BLOCK_INTERACTION_RANGE,
                getConfigDouble("attribute_amount"),
                AttributeModifier.Operation.ADD_NUMBER
        );
    }
}
