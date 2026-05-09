package justjabka.WeltenRaces.Modifiers.Contents.Armor;

import justjabka.WeltenRaces.Configs.Modifier.Armor.IronArmorModifierConfig;
import justjabka.WeltenRaces.Modifiers.Contents.Generic.BaseArmorModifier;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public class IronArmorModifier extends BaseArmorModifier {
    public IronArmorModifier(String id, IronArmorModifierConfig config) {
        super(
                id,
                Attribute.ARMOR_TOUGHNESS,
                config.attributeAmount,
                AttributeModifier.Operation.ADD_NUMBER
        );
    }
}
