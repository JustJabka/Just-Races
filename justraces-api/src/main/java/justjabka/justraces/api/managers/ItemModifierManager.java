package justjabka.justraces.api.managers;

import io.papermc.paper.persistence.PersistentDataContainerView;
import justjabka.justraces.api.JustRacesAPI;
import justjabka.justraces.api.JustRacesRegistries;
import justjabka.justraces.api.common.definition.RaceDefinition;
import justjabka.justraces.api.itemmodifiers.generic.BaseItemModifier;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public final class ItemModifierManager {

    private ItemModifierManager() {}

    public static final NamespacedKey ITEM_MODIFIED_KEY = new NamespacedKey(JustRacesAPI.NAMESPACE, "item_modified");

    @Nullable
    public static BaseItemModifier getByKey(NamespacedKey key) {
        return JustRacesRegistries.ITEM_MODIFIERS.get(key);
    }

    @Nullable
    public static BaseItemModifier getItemModifierForMaterialRace(Player player, ItemStack item) {
        RaceDefinition race = RaceManager.getRace(player);
        return race.getItemModifierForMaterial(item.getType());
    }

    @Nullable
    public static BaseItemModifier getItemModifierForMaterialPlayer(Player player, ItemStack item) {
        BaseItemModifier raceItemModifier = getItemModifierForMaterialRace(player, item);
        if (raceItemModifier != null) return raceItemModifier;

        Map<Material, BaseItemModifier> transientModifiers = TransientManager.getTransientItemModifiers(player).modifiers();
        return transientModifiers.get(item.getType());
    }

    public static void tryApply(Player player, ItemStack item) {
        if (item == null) return;
        if (item.isEmpty()) return;

        BaseItemModifier modifier = getItemModifierForMaterialPlayer(player, item);

        if (modifier == null) return;
        if (isModifiedWith(item, modifier)) return;
        if (isCustomItem(item)) return;

        modifier.apply(item);
        addMarker(item, modifier);
    }

    public static void tryUndo(ItemStack item) {
        if (item == null) return;
        if (item.isEmpty()) return;
        if (!item.getPersistentDataContainer().has(ITEM_MODIFIED_KEY)) return;

        BaseItemModifier modifier = getAppliedModifier(item);

        if (modifier == null) {
            removeMarker(item);

            JustRacesAPI.getLogger().warn("Tried to undo unknown or unregistered modifier");
            return;
        }

        try {
            modifier.undo(item);
            removeMarker(item);
        } catch (IllegalArgumentException e) {
            JustRacesAPI.getLogger().error("Error while undoing modifier {} on item {}", modifier.getKey(), item.getType(), e);
        }
    }

    public static void tryUndoInventory(Inventory inventory) {
        for (ItemStack item : inventory.getContents()) {
            if (item == null) continue;
            if (item.isEmpty()) continue;
            ItemModifierManager.tryUndo(item);
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

    public static boolean isCustomItem(ItemStack item) {
        PersistentDataContainerView pdc = item.getPersistentDataContainer();
        if (pdc.isEmpty()) return false;

        boolean hasOnlyMarker = pdc.getSize() == 1 && pdc.has(ITEM_MODIFIED_KEY);
        return !hasOnlyMarker;
    }

    private static void addMarker(ItemStack item, BaseItemModifier modifier) {
        item.editPersistentDataContainer(pdc ->
                pdc.set(ITEM_MODIFIED_KEY, PersistentDataType.STRING, modifier.getKey().toString())
        );
    }

    private static void removeMarker(ItemStack item) {
        item.editPersistentDataContainer(pdc ->
                pdc.remove(ITEM_MODIFIED_KEY)
        );
    }

    public static boolean isModifiedWith(ItemStack item, BaseItemModifier modifier) {
        if (modifier == null) return false;
        return modifier.equals(getAppliedModifier(item));
    }

    @Nullable
    public static BaseItemModifier getAppliedModifier(ItemStack item) {
        PersistentDataContainerView pdc = item.getPersistentDataContainer();

        String keyStr = pdc.get(ITEM_MODIFIED_KEY, PersistentDataType.STRING);
        if (keyStr == null) return null;

        NamespacedKey key = NamespacedKey.fromString(keyStr, JustRacesAPI.getInstance());
        if (key == null) return null;

        return getByKey(key);
    }
}