package justjabka.WeltenRaces.Managers;

import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class ItemManager {
    private static final NamespacedKey ITEM_MODIFIED_KEY = new NamespacedKey(WeltenRaces.PLUGIN_ID, "item_modified");

    public static void addItemModifier(ItemStack item) {
        if (isItemModified(item)) return;

        item.editPersistentDataContainer(pdc -> {
            pdc.set(ITEM_MODIFIED_KEY, PersistentDataType.BOOLEAN, true);
        });
    }

    public static void removeItemModifier(ItemStack item) {
        if (!isItemModified(item)) return;

        item.editPersistentDataContainer(pdc -> {
            pdc.remove(ITEM_MODIFIED_KEY);
        });
    }

    private static boolean isItemModified(ItemStack item) {
        return item.getPersistentDataContainer().has(ITEM_MODIFIED_KEY);
    }
}