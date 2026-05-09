package justjabka.WeltenRaces.Modifiers.Contents.Armor;

import justjabka.WeltenRaces.Configs.Modifier.Armor.ChainmailArmorModifierConfig;
import justjabka.WeltenRaces.Modifiers.Contents.Generic.BaseArmorModifier;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public class ChainmailArmorModifier extends BaseArmorModifier {
    public ChainmailArmorModifier(String id, ChainmailArmorModifierConfig config) {
        super(
                id,
                Attribute.LUCK,
                config.attributeAmount,
                AttributeModifier.Operation.ADD_NUMBER
        );
    }
}
