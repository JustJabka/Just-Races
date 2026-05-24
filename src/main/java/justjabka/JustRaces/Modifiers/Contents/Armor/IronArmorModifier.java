package justjabka.JustRaces.Modifiers.Contents.Armor;

import justjabka.JustRaces.Configs.Modifier.Armor.IronArmorModifierConfig;
import justjabka.JustRaces.Modifiers.Contents.Generic.BaseArmorModifier;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public class IronArmorModifier extends BaseArmorModifier {
    public IronArmorModifier(NamespacedKey key, IronArmorModifierConfig config) {
        super(
                key,
                Attribute.ARMOR_TOUGHNESS,
                config.attributeAmount,
                AttributeModifier.Operation.ADD_NUMBER
        );
    }
}
