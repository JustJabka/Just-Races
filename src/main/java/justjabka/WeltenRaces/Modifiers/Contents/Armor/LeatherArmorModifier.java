package justjabka.WeltenRaces.Modifiers.Contents.Armor;

import justjabka.WeltenRaces.Configs.Modifier.Armor.LeatherArmorModifierConfig;
import justjabka.WeltenRaces.Modifiers.Contents.Generic.BaseArmorModifier;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public class LeatherArmorModifier extends BaseArmorModifier {
    public LeatherArmorModifier(String id, LeatherArmorModifierConfig config) {
        super(
                id,
                Attribute.MOVEMENT_SPEED,
                config.attributeAmount,
                AttributeModifier.Operation.ADD_NUMBER
        );
    }
}
