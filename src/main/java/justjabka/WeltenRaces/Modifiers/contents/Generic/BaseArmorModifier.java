package justjabka.WeltenRaces.Modifiers.contents.Generic;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemAttributeModifiers;
import justjabka.WeltenRaces.Modifiers.RaceModifier;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import static justjabka.WeltenRaces.Managers.ModifierManager.ITEM_MODIFIED_KEY;

@SuppressWarnings("UnstableApiUsage")
public class BaseArmorModifier implements RaceModifier {
    private final String id;
    private final Attribute attribute;
    private final double amount;
    private final AttributeModifier.Operation operation;

    public BaseArmorModifier(
            String id,
            Attribute attribute,
            double amount,
            AttributeModifier.Operation operation
    ) {
        this.id = id;
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

        // Add Marker
        item.editPersistentDataContainer(pdc ->
                pdc.set(ITEM_MODIFIED_KEY, PersistentDataType.STRING, id)
        );
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

        // Remove Marker
        item.editPersistentDataContainer(pdc ->
                pdc.remove(ITEM_MODIFIED_KEY)
        );
    }

    private NamespacedKey getDynamicKey(ItemStack item) {
        return new NamespacedKey(WeltenRaces.PLUGIN_ID, id.toLowerCase() + "." + getItemGroupSlot(item));
    }

    private EquipmentSlotGroup getItemGroupSlot(ItemStack item) {
        return item.getData(DataComponentTypes.EQUIPPABLE).slot().getGroup();
    }
}
