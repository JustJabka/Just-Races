package justjabka.JustRaces.Modifiers.Armor;

import justjabka.JustRaces.Configs.Modifier.Armor.ChainmailArmorModifierConfig;
import justjabka.JustRaces.Modifiers.Contents.Generic.BaseArmorModifier;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public class ChainmailArmorModifier extends BaseArmorModifier {
    public ChainmailArmorModifier(NamespacedKey key, ChainmailArmorModifierConfig config) {
        super(
                key,
                Attribute.LUCK,
                config.attributeAmount,
                AttributeModifier.Operation.ADD_NUMBER
        );
    }
}
