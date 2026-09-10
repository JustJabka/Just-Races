package justjabka.justraces.api.managers;

import justjabka.justraces.api.definitions.RaceDefinition;
import justjabka.justraces.api.JustRacesAPI;
import justjabka.justraces.api.JustRacesRegistries;
import justjabka.justraces.api.modifiers.generic.BaseModifier;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public final class ModifierManager {

    private ModifierManager() {}

    public static final NamespacedKey ITEM_MODIFIED_KEY = new NamespacedKey(JustRacesAPI.NAMESPACE, "item_modified");

    public static BaseModifier getByKey(NamespacedKey key) {
        return JustRacesRegistries.MODIFIERS.get(key);
    }

    public static void tryApply(Player player, ItemStack item) {
        if (item == null) return;
        if (item.isEmpty()) return;

        RaceDefinition race = RaceManager.getRace(player);
        BaseModifier type = race.getModifier(item.getType());

        if (type == null) return;
        if (isModifiedWith(item, type)) return;

        type.apply(item);
        addMarker(item, type);
    }

    public static void tryUndo(ItemStack item) {
        if (item == null) return;
        if (!item.hasItemMeta()) return;

        String modifierId = getAppliedModifier(item);
        if (modifierId == null) return;

        NamespacedKey key = NamespacedKey.fromString(modifierId, JustRacesAPI.getInstance());
        if (key == null) return;

        BaseModifier type = getByKey(key);

        if (type == null) {
            removeMarker(item);

            JustRacesAPI.getLogger().warn("Tried to undo unknown or unregistered modifier: {}", modifierId);
            return;
        }

        try {
            type.undo(item);
            removeMarker(item);
        } catch (IllegalArgumentException e) {
            JustRacesAPI.getLogger().error("Error while undoing modifier {} on item {}", modifierId, item.getType(), e);
        }
    }

    public static void tryUndoInventory(ItemStack[] inventoryContents) {
        for (ItemStack item : inventoryContents) {
            if (item == null) continue;
            ModifierManager.tryUndo(item);
        }
    }

    public static void refreshModifiersOnItem(Player player, ItemStack item) {
        if (item == null) return;
        if (item.isEmpty()) return;

        tryUndo(item);
        tryApply(player, item);
    }

    public static void refreshModifiers(Player player) {
        ItemStack cursorItem = player.getItemOnCursor();
        ItemStack[] inventoryContents = player.getInventory().getContents();

        for (ItemStack item : inventoryContents) {
            refreshModifiersOnItem(player, item);
        }

        refreshModifiersOnItem(player, cursorItem);
    }

    private static void addMarker(ItemStack item, BaseModifier type) {
        item.editPersistentDataContainer(pdc ->
                pdc.set(ITEM_MODIFIED_KEY, PersistentDataType.STRING, type.getKey().toString())
        );
    }

    private static void removeMarker(ItemStack item) {
        item.editPersistentDataContainer(pdc ->
                pdc.remove(ITEM_MODIFIED_KEY)
        );
    }

    public static boolean isModifiedWith(ItemStack item, BaseModifier type) {
        if (type == null) return false;

        String modifier = getAppliedModifier(item);
        return type.getKey().asString().equals(modifier);
    }

    public static String getAppliedModifier(ItemStack item) {
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        return pdc.get(ITEM_MODIFIED_KEY, PersistentDataType.STRING);
    }
}