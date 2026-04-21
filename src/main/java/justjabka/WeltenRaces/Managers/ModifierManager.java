package justjabka.WeltenRaces.Managers;

import justjabka.WeltenRaces.Types.ModifierType;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ModifierManager {
    public static final NamespacedKey ITEM_MODIFIED_KEY = new NamespacedKey(WeltenRaces.PLUGIN_ID, "item_modified");

    public static void tryApply(Player player, ItemStack item) {
        if (item == null) return;
        if (item.isEmpty()) return;

        ModifierType type = RaceManager.getRace(player).getModifierFor(item.getType());

        if (type == null) return;
        if (isAlreadyModified(item, type)) return;

        type.get().apply(item);
        item.editMeta(meta -> meta
                .getPersistentDataContainer()
                .set(ITEM_MODIFIED_KEY, PersistentDataType.STRING, type.name()));
    }

    public static void tryUndo(ItemStack item) {
        if (item == null) return;
        if (!item.hasItemMeta()) return;

        String modifier = getModifier(item);

        if (modifier == null) return;

        try {
            ModifierType type = ModifierType.valueOf(modifier);
            type.get().undo(item);
            item.editMeta(meta -> meta.getPersistentDataContainer().remove(ITEM_MODIFIED_KEY));
        } catch (IllegalArgumentException e) {
            WeltenRaces.LOGGER.warn("Unknown modifier: {}", modifier);
        }
    }

    public static void tryUndoInventory(ItemStack[] inventoryContents) {
        for (ItemStack item : inventoryContents) {
            if (item == null) continue;
            ModifierManager.tryUndo(item);
        }
    }

    public static void refreshModifiers(Player player) {
        ItemStack cursor = player.getItemOnCursor();
        ItemStack[] inventoryContents = player.getInventory().getContents();

        for (ItemStack item : inventoryContents) {
            if (item == null) continue;
            if (item.getType().isAir()) continue;

            ModifierManager.tryUndo(item);
            ModifierManager.tryApply(player, item);
        }

        if (!cursor.getType().isAir()) {
            ModifierManager.tryUndo(cursor);
            ModifierManager.tryApply(player, cursor);
        }
    }

    private static boolean isAlreadyModified(ItemStack item, ModifierType type) {
        String modifier = getModifier(item);
        return type.name().equals(modifier);
    }

    private static String getModifier(ItemStack item) {
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();

        return pdc.get(ITEM_MODIFIED_KEY, PersistentDataType.STRING);
    }
}