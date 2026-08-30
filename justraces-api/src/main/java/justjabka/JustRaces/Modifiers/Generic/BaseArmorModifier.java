package justjabka.JustRaces.Modifiers.Generic;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemAttributeModifiers;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("UnstableApiUsage")
public abstract class BaseArmorModifier extends BaseModifier {
    public abstract Attribute getAttribute();
    public abstract double getAttributeAmount();
    public abstract AttributeModifier.Operation getAttributeOperation();

    @Override
    public void apply(ItemStack item) {
        // Filter Attributes
        ItemAttributeModifiers.Builder attributes = ItemAttributeModifiers.itemAttributes();
        item.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS).modifiers()
                .stream()
                .filter(entry -> !entry.modifier().key().equals(getDynamicKey(item)))
                .forEach(entry ->
                        attributes.addModifier(entry.attribute(), entry.modifier(), entry.getGroup(), entry.display())
                );

        // Apply Modifier
        attributes.addModifier(getAttribute(), new AttributeModifier(
                getDynamicKey(item),
                getAttributeAmount(),
                getAttributeOperation(),
                getItemGroupSlot(item)
        ));

        // Build Attributes
        item.setData(DataComponentTypes.ATTRIBUTE_MODIFIERS, attributes.build());
    }

    @Override
    public void undo(ItemStack item) {
        ItemAttributeModifiers currentAttributes = item.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS);

        if (currentAttributes != null) {
            // Filter and Build Attributes
            ItemAttributeModifiers.Builder attributes = ItemAttributeModifiers.itemAttributes();
            currentAttributes.modifiers().stream()
                    .filter(entry -> !entry.modifier().key().equals(getDynamicKey(item)))
                    .forEach(entry -> attributes.addModifier(entry.attribute(), entry.modifier(), entry.getGroup(), entry.display()));

            item.setData(DataComponentTypes.ATTRIBUTE_MODIFIERS, attributes.build());
        }
    }

    private NamespacedKey getDynamicKey(ItemStack item) {
        return new NamespacedKey(getKey().getNamespace(), getKey().getKey() + "." + getItemGroupSlot(item));
    }

    private EquipmentSlotGroup getItemGroupSlot(ItemStack item) {
        return item.getData(DataComponentTypes.EQUIPPABLE).slot().getGroup();
    }
}
