package justjabka.JustRacesShowcase.Modifiers.Armor;

import justjabka.JustRacesShowcase.Configs.Modifier.Armor.GoldenArmorModifierConfig;
import justjabka.JustRaces.Modifiers.Generic.BaseArmorModifier;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public class GoldenArmorModifier extends BaseArmorModifier {
    public GoldenArmorModifier(NamespacedKey key, GoldenArmorModifierConfig config) {
        super(
                key,
                Attribute.MAX_ABSORPTION,
                config.attributeAmount,
                AttributeModifier.Operation.ADD_NUMBER
        );
    }
}
