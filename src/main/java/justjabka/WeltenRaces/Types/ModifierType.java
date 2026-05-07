package justjabka.WeltenRaces.Types;

import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import io.papermc.paper.datacomponent.item.UseCooldown;
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation;
import justjabka.WeltenRaces.Modifiers.Contents.Generic.BaseArmorModifier;
import justjabka.WeltenRaces.Modifiers.Contents.Generic.BaseFoodCooldownModifier;
import justjabka.WeltenRaces.Modifiers.Contents.Generic.BaseFoodModifier;
import justjabka.WeltenRaces.Modifiers.RaceModifier;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

@SuppressWarnings("UnstableApiUsage")
public enum ModifierType {
    // Armor
    LEATHER_ARMOR(new BaseArmorModifier("LEATHER_ARMOR", Attribute.MOVEMENT_SPEED, 0.01, AttributeModifier.Operation.ADD_NUMBER)),
    COPPER_ARMOR(new BaseArmorModifier("COPPER_ARMOR", Attribute.BLOCK_INTERACTION_RANGE, 0.5, AttributeModifier.Operation.ADD_NUMBER)),
    CHAINMAIL_ARMOR(new BaseArmorModifier("CHAINMAIL_ARMOR", Attribute.LUCK, 1, AttributeModifier.Operation.ADD_NUMBER)),
    IRON_ARMOR(new BaseArmorModifier("IRON_ARMOR", Attribute.ARMOR_TOUGHNESS, 0.5, AttributeModifier.Operation.ADD_NUMBER)),
    GOLDEN_ARMOR(new BaseArmorModifier("GOLDEN_ARMOR", Attribute.MAX_ABSORPTION, 1, AttributeModifier.Operation.ADD_NUMBER)),

    // Food
    PHANTOM_MEMBRANE(new BaseFoodModifier(
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
    )),
    GLOW_BERRIES(new BaseFoodModifier(
            "GLOW_BERRIES",
            FoodProperties.food()
            .nutrition(4 + 2)
            .saturation(0.4f)
            .build(),

            Consumable.consumable().build()
    )),
    CONSUMABLE_MOSS(new BaseFoodCooldownModifier(
            "CONSUMABLE_MOSS",
            FoodProperties.food()
            .nutrition(0)
            .saturation(0)
            .canAlwaysEat(true)
            .build(),
            Consumable.consumable().build(),
            UseCooldown.useCooldown(60).build()
    ));

    private final RaceModifier modifier;

    ModifierType(RaceModifier modifier) {
        this.modifier = modifier;
    }

    public RaceModifier get() {
        return modifier;
    }
}
