package justjabka.WeltenRaces.Managers;

import justjabka.WeltenRaces.Instances.RaceInstance;
import justjabka.WeltenRaces.Modifiers.ItemModifier;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;

import static justjabka.WeltenRaces.Registries.ModifiersRegistry.MODIFIERS_BY_KEY;
import static justjabka.WeltenRaces.Registries.ModifiersRegistry.RACE_MODIFIERS;

public class ModifierManager {
    public static final NamespacedKey ITEM_MODIFIED_KEY = new NamespacedKey(WeltenRaces.NAMESPACE, "item_modified");

    public static ItemModifier getModifiersForRace(RaceInstance race, ItemStack item) {
        return RACE_MODIFIERS.getOrDefault(race.getKey(), Map.of()).get(item.getType());
    }

    public static ItemModifier getByKey(NamespacedKey key) {
        return MODIFIERS_BY_KEY.get(key);
    }

    public static void tryApply(Player player, ItemStack item) {
        if (item == null) return;
        if (item.isEmpty()) return;

        RaceInstance race = RaceManager.getRace(player);
        ItemModifier type = getModifiersForRace(race, item);

        if (type == null) return;
        if (isModifiedWith(item, type)) return;

        type.apply(item);
        item.editMeta(meta -> meta
                .getPersistentDataContainer()
                .set(ITEM_MODIFIED_KEY, PersistentDataType.STRING, type.getKey().toString()));
    }

    public static void tryUndo(ItemStack item) {
        if (item == null) return;
        if (!item.hasItemMeta()) return;

        String modifierId = getAppliedModifier(item);
        if (modifierId == null) return;

        NamespacedKey key = NamespacedKey.fromString(modifierId, WeltenRaces.INSTANCE);
        if (key == null) return;

        ItemModifier type = getByKey(key);

        if (type == null) {
            item.editMeta(meta -> meta.getPersistentDataContainer().remove(ITEM_MODIFIED_KEY));

            WeltenRaces.LOGGER.warn("Tried to undo unknown or unregistered modifier: {}", modifierId);
            return;
        }

        try {
            type.undo(item);
            item.editMeta(meta -> meta.getPersistentDataContainer().remove(ITEM_MODIFIED_KEY));
        } catch (IllegalArgumentException e) {
            WeltenRaces.LOGGER.error("Error while undoing modifier {} on item {}", modifierId, item.getType(), e);
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

    private static boolean isModifiedWith(ItemStack item, ItemModifier type) {
        String modifier = getAppliedModifier(item);
        return type.getKey().asString().equals(modifier);
    }

    public static String getAppliedModifier(ItemStack item) {
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        return pdc.get(ITEM_MODIFIED_KEY, PersistentDataType.STRING);
    }
}