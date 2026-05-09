package justjabka.WeltenRaces.Modifiers.Contents.Armor;

import justjabka.WeltenRaces.Configs.Modifier.Armor.CopperArmorModifierConfig;
import justjabka.WeltenRaces.Modifiers.Contents.Generic.BaseArmorModifier;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public class CopperArmorModifier extends BaseArmorModifier {
    public CopperArmorModifier(String id, CopperArmorModifierConfig config) {
        super(
                id,
                Attribute.BLOCK_INTERACTION_RANGE,
                config.attributeAmount,
                AttributeModifier.Operation.ADD_NUMBER
        );
    }
}
