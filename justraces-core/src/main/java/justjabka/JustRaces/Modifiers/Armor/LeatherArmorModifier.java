package justjabka.JustRaces.Modifiers.Armor;

import justjabka.JustRaces.Configs.Modifier.Armor.LeatherArmorModifierConfig;
import justjabka.JustRaces.Modifiers.Contents.Generic.BaseArmorModifier;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public class LeatherArmorModifier extends BaseArmorModifier {
    public LeatherArmorModifier(NamespacedKey key, LeatherArmorModifierConfig config) {
        super(
                key,
                Attribute.MOVEMENT_SPEED,
                config.attributeAmount,
                AttributeModifier.Operation.ADD_NUMBER
        );
    }
}
