package justjabka.justraces.showcase.modifiers.armor;

import justjabka.justraces.api.itemmodifiers.generic.BaseArmorItemModifier;
import justjabka.justraces.api.itemmodifiers.generic.ConfigurableItemModifier;
import justjabka.justraces.showcase.JustRacesShowcase;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.jetbrains.annotations.Nullable;

public class GoldenArmorItemModifier extends BaseArmorItemModifier implements ConfigurableItemModifier {

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "armor/golden");
    }

    @Override
    public @Nullable UnkeyedAttributeModifier getAttributeModifier() {
        return new UnkeyedAttributeModifier(
                Attribute.MAX_ABSORPTION,
                getConfigDouble("attribute_amount"),
                AttributeModifier.Operation.ADD_NUMBER
        );
    }
}
