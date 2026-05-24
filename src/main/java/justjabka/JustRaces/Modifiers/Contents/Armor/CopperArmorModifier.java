package justjabka.JustRaces.Modifiers.Contents.Armor;

import justjabka.JustRaces.Configs.Modifier.Armor.CopperArmorModifierConfig;
import justjabka.JustRaces.Modifiers.Contents.Generic.BaseArmorModifier;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public class CopperArmorModifier extends BaseArmorModifier {
    public CopperArmorModifier(NamespacedKey key, CopperArmorModifierConfig config) {
        super(
                key,
                Attribute.BLOCK_INTERACTION_RANGE,
                config.attributeAmount,
                AttributeModifier.Operation.ADD_NUMBER
        );
    }
}
