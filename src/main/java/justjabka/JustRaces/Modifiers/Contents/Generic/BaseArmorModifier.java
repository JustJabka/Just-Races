package justjabka.JustRaces.Modifiers.Contents.Generic;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemAttributeModifiers;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("UnstableApiUsage")
public class BaseArmorModifier extends BaseModifier {
    private final Attribute attribute;
    private final double amount;
    private final AttributeModifier.Operation operation;

    public BaseArmorModifier(
            NamespacedKey key,
            Attribute attribute,
            double amount,
            AttributeModifier.Operation operation
    ) {
        super(key);
        this.attribute = attribute;
        this.amount = amount;
        this.operation = operation;
    }

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
        attributes.addModifier(this.attribute, new AttributeModifier(
                getDynamicKey(item),
                this.amount,
                this.operation,
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
