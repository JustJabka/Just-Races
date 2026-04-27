package justjabka.WeltenRaces.Types;

import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation;
import justjabka.WeltenRaces.Modifiers.Contents.*;
import justjabka.WeltenRaces.Modifiers.RaceModifier;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

@SuppressWarnings("UnstableApiUsage")
public enum ModifierType {
    // Armor
    LEATHER_ARMOR(new LeatherArmorModifier("LEATHER_ARMOR", Attribute.MOVEMENT_SPEED, 0.01, AttributeModifier.Operation.ADD_NUMBER)),
    COPPER_ARMOR(new CopperArmorModifier("COPPER_ARMOR", Attribute.BLOCK_INTERACTION_RANGE, 0.5, AttributeModifier.Operation.ADD_NUMBER)),
    CHAINMAIL_ARMOR(new ChainmailArmorModifier("CHAINMAIL_ARMOR", Attribute.LUCK, 1, AttributeModifier.Operation.ADD_NUMBER)),
    IRON_ARMOR(new IronArmorModifier("IRON_ARMOR", Attribute.ARMOR_TOUGHNESS, 0.5, AttributeModifier.Operation.ADD_NUMBER)),
    GOLDEN_ARMOR(new GoldenArmorModifier("GOLDEN_ARMOR", Attribute.MAX_ABSORPTION, 1, AttributeModifier.Operation.ADD_NUMBER)),

    // Food
    PHANTOM_MEMBRANE(new PhantomMembraneModifier(
            "PHANTOM_MEMBRANE",
            FoodProperties.food()
            .canAlwaysEat(true)
            .nutrition(0)
            .saturation(0)
            .build(),

            Consumable.consumable()
            .consumeSeconds(0.8f)
            .animation(ItemUseAnimation.EAT)
            .hasConsumeParticles(true)
            .build()
    ));

    private final RaceModifier modifier;

    ModifierType(RaceModifier modifier) {
        this.modifier = modifier;
    }

    public RaceModifier get() {
        return modifier;
    }
}
