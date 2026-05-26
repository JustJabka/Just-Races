package justjabka.JustRaces.Managers;

import justjabka.JustRaces.Instances.RaceInstance;
import justjabka.JustRaces.JustRacesAPI;
import justjabka.JustRaces.JustRacesRegistries;
import justjabka.JustRaces.Modifiers.ItemModifier;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModifierManager {
    public static final NamespacedKey ITEM_MODIFIED_KEY = new NamespacedKey(JustRacesAPI.NAMESPACE, "item_modified");
    private static final Map<NamespacedKey, Map<Material, ItemModifier>> ACTIVE_RACE_MODIFIERS = new ConcurrentHashMap<>();

    @Nullable
    public static ItemModifier getModifiersForRace(@NotNull RaceInstance race, @NotNull ItemStack item) {
        return ACTIVE_RACE_MODIFIERS.getOrDefault(race.getKey(), Map.of()).get(item.getType());
    }

    @NotNull
    public static Map<NamespacedKey, Map<Material, ItemModifier>> getActiveRaceModifiers() {
        return Collections.unmodifiableMap(ACTIVE_RACE_MODIFIERS);
    }

    public static void updateRaceModifiers(@NotNull Map<NamespacedKey, Map<Material, ItemModifier>> newBindings) {
        ACTIVE_RACE_MODIFIERS.clear();
        newBindings.forEach((raceKey, matMap) ->
                ACTIVE_RACE_MODIFIERS.computeIfAbsent(raceKey, k -> new ConcurrentHashMap<>()).putAll(matMap)
        );
    }

    public static ItemModifier getByKey(NamespacedKey key) {
        return JustRacesRegistries.MODIFIERS.get(key);
    }

    public static void tryApply(Player player, ItemStack item) {
        if (item == null) return;
        if (item.isEmpty()) return;

        RaceInstance race = RaceManager.getRace(player);
        ItemModifier type = getModifiersForRace(race, item);

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

        ItemModifier type = getByKey(key);

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

    private static void addMarker(ItemStack item, ItemModifier type) {
        item.editPersistentDataContainer(pdc ->
                pdc.set(ITEM_MODIFIED_KEY, PersistentDataType.STRING, type.getKey().toString())
        );
    }

    private static void removeMarker(ItemStack item) {
        item.editPersistentDataContainer(pdc ->
                pdc.remove(ITEM_MODIFIED_KEY)
        );
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