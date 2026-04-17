package justjabka.WeltenRaces.Modifiers.contents;

import justjabka.WeltenRaces.Modifiers.RaceModifier;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import static justjabka.WeltenRaces.Managers.ItemManager.ITEM_MODIFIED_KEY;

public class LeatherArmorModifier implements RaceModifier {
    private final NamespacedKey attributeKey = new NamespacedKey(WeltenRaces.PLUGIN_ID, "leather_armor_modifier");
    private final AttributeModifier attributeModifier = new AttributeModifier(attributeKey, 0.01, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.ARMOR);

    @Override
    public void apply(ItemStack item) {
        item.editMeta(meta -> {
            meta.addAttributeModifier(
                    Attribute.MOVEMENT_SPEED,
                    attributeModifier
            );

            meta.getPersistentDataContainer().set(ITEM_MODIFIED_KEY, PersistentDataType.STRING, "leather_armor_modifier");
        });
    }

    @Override
    public void undo(ItemStack item) {
        item.editMeta(meta -> {
            meta.removeAttributeModifier(Attribute.MOVEMENT_SPEED, attributeModifier);
            meta.getPersistentDataContainer().remove(ITEM_MODIFIED_KEY);
        });
    }
}