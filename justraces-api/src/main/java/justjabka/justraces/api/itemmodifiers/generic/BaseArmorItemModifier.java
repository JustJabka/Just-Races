package justjabka.justraces.api.itemmodifiers.generic;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Equippable;
import io.papermc.paper.datacomponent.item.ItemAttributeModifiers;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@SuppressWarnings("UnstableApiUsage")
public abstract class BaseArmorItemModifier extends BaseItemModifier {

    public @Nullable UnkeyedAttributeModifier getAttributeModifier() {
        return null;
    }

    public @Nullable Consumer<Equippable.Builder> getEquippable() {
        return null;
    }
    public @NotNull EquipmentSlot getEquippableSlotFallBack() {
        return EquipmentSlot.HAND;
    }

    @Override
    public void apply(ItemStack item) {
        applyEquippable(item);
        applyAttributeModifier(item);
    }

    @Override
    public void undo(ItemStack item) {
        undoEquippable(item);
        undoAttributeModifier(item);
    }

    private void applyEquippable(ItemStack item) {
        if (getEquippable() == null) return;
        mergeComponent(
                item,
                DataComponentTypes.EQUIPPABLE,
                getEquippable(),
                Equippable.equippable(getEquippableSlotFallBack())
        );
    }

    private void undoEquippable(ItemStack item) {
        if (getEquippable() == null) return;
        item.resetData(DataComponentTypes.EQUIPPABLE);
    }

    private void applyAttributeModifier(ItemStack item) {
        UnkeyedAttributeModifier modifier = getAttributeModifier();
        if (modifier == null) return;

        ItemAttributeModifiers.Builder attributes = ItemAttributeModifiers.itemAttributes();

        item.getDataOrDefault(DataComponentTypes.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.itemAttributes().build()).modifiers()
                .stream()
                .filter(entry -> !entry.modifier().key().equals(getDynamicKey(item)))
                .forEach(entry ->
                        attributes.addModifier(entry.attribute(), entry.modifier(), entry.getGroup(), entry.display())
                );

        // Apply Modifier
        attributes.addModifier(modifier.attribute(), new AttributeModifier(
                getDynamicKey(item),
                modifier.amount(),
                modifier.operation(),
                getItemGroupSlot(item)
        ));

        // Build Attributes
        item.setData(DataComponentTypes.ATTRIBUTE_MODIFIERS, attributes.build());
    }

    private void undoAttributeModifier(ItemStack item) {
        UnkeyedAttributeModifier modifier = getAttributeModifier();
        if (modifier == null) return;

        ItemAttributeModifiers currentAttributes = item.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS);
        if (currentAttributes == null) return;

        // Filter and Build Attributes
        ItemAttributeModifiers.Builder attributes = ItemAttributeModifiers.itemAttributes();
        currentAttributes.modifiers().stream()
                .filter(entry -> !entry.modifier().key().equals(getDynamicKey(item)))
                .forEach(entry -> attributes.addModifier(entry.attribute(), entry.modifier(), entry.getGroup(), entry.display()));

        item.setData(DataComponentTypes.ATTRIBUTE_MODIFIERS, attributes.build());
    }

    private NamespacedKey getDynamicKey(ItemStack item) {
        String dynamicKey = "%s.%s".formatted(getKey(), getItemGroupSlot(item));
        return NamespacedKey.fromString(dynamicKey);
    }

    private EquipmentSlotGroup getItemGroupSlot(ItemStack item) {
        Equippable equippable = item.getData(DataComponentTypes.EQUIPPABLE);
        if (equippable == null) return getEquippableSlotFallBack().getGroup();

        return equippable.slot().getGroup();
    }

    public record UnkeyedAttributeModifier(
            Attribute attribute,
            double amount,
            AttributeModifier.Operation operation
    ) {
    }
}
