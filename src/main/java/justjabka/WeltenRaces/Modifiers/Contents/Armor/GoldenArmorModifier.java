package justjabka.WeltenRaces.Modifiers.Contents.Armor;

import justjabka.WeltenRaces.Configs.Modifier.Armor.GoldenArmorModifierConfig;
import justjabka.WeltenRaces.Modifiers.Contents.Generic.BaseArmorModifier;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public class GoldenArmorModifier extends BaseArmorModifier {
    public GoldenArmorModifier(String id, GoldenArmorModifierConfig config) {
        super(
                id,
                Attribute.MAX_ABSORPTION,
                config.attributeAmount,
                AttributeModifier.Operation.ADD_NUMBER
        );
    }
}
