package justjabka.WeltenRaces.Modifiers.contents;

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

import static justjabka.WeltenRaces.Managers.ItemManager.ITEM_MODIFIED_KEY;

@SuppressWarnings("UnstableApiUsage")
public class LeatherArmorModifier implements RaceModifier {
    private final String id;
    private final NamespacedKey attributeKey;
    private final AttributeModifier attributeModifier;

    public LeatherArmorModifier(String id) {
        this.id = id;
        this.attributeKey = new NamespacedKey(WeltenRaces.PLUGIN_ID, id.toLowerCase());
        this.attributeModifier = new AttributeModifier(
                attributeKey,
                0.01,
                AttributeModifier.Operation.ADD_NUMBER,
                EquipmentSlotGroup.ARMOR
        );
    }

    @Override
    public void apply(ItemStack item) {
        ItemAttributeModifiers.Builder attributes = ItemAttributeModifiers.itemAttributes();
                item.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS).modifiers()
                .stream()
                .filter(entry -> !entry.modifier().key().equals(attributeKey))
                .forEach(entry ->
                    attributes.addModifier(entry.attribute(), entry.modifier(), entry.getGroup(), entry.display())
                );

        attributes.addModifier(Attribute.MOVEMENT_SPEED, attributeModifier);

        item.setData(DataComponentTypes.ATTRIBUTE_MODIFIERS, attributes.build());

        item.editPersistentDataContainer(pdc ->
                pdc.set(ITEM_MODIFIED_KEY, PersistentDataType.STRING, id)
        );
    }

    @Override
    public void undo(ItemStack item) {
        ItemAttributeModifiers current = item.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS);
        if (current != null) {
            ItemAttributeModifiers.Builder attributes = ItemAttributeModifiers.itemAttributes();
            current.modifiers().stream()
                    .filter(entry -> !entry.modifier().key().equals(attributeKey))
                    .forEach(entry -> attributes.addModifier(entry.attribute(), entry.modifier(), entry.getGroup(), entry.display()));

            item.setData(DataComponentTypes.ATTRIBUTE_MODIFIERS, attributes.build());
        }

        item.editPersistentDataContainer(pdc ->
                pdc.remove(ITEM_MODIFIED_KEY)
        );
    }
}