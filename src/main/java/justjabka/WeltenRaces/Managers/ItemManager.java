package justjabka.WeltenRaces.Managers;

import justjabka.WeltenRaces.Types.ModifierType;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class ItemManager {
    public static final NamespacedKey ITEM_MODIFIED_KEY = new NamespacedKey(WeltenRaces.PLUGIN_ID, "item_modified");

    public static void tryApply(Player player, ItemStack item) {
        if (item == null || item.isEmpty()) return;

        ModifierType type = RaceManager.getRace(player).getModifierFor(item.getType());

        if (type != null) {
            type.get().apply(item);
            item.editMeta(meta -> meta.getPersistentDataContainer()
                    .set(ITEM_MODIFIED_KEY, PersistentDataType.STRING, type.name()));
        }
    }

    public static void tryUndo(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return;

        String modName = item.getItemMeta().getPersistentDataContainer()
                .get(ITEM_MODIFIED_KEY, PersistentDataType.STRING);

        if (modName != null) {
            try {
                ModifierType type = ModifierType.valueOf(modName);
                type.get().undo(item);
                item.editMeta(meta -> meta.getPersistentDataContainer().remove(ITEM_MODIFIED_KEY));
            } catch (IllegalArgumentException e) {
                WeltenRaces.LOGGER.warn("Unknown modifier: {}", modName);
            }
        }
    }
}