package justjabka.justraces.api.common;

import com.jeff_media.morepersistentdatatypes.DataType;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface PersistentHolder {
    NamespacedKey getKey();
    NamespacedKey getContainerKey();

    @NotNull
    default PersistentDataContainer getContainer(Player player) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        return pdc.getOrDefault(
                getContainerKey(),
                PersistentDataType.TAG_CONTAINER,
                pdc.getAdapterContext().newPersistentDataContainer()
        );
    }

    default void saveContainer(Player player, PersistentDataContainer container) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        pdc.set(
                getContainerKey(),
                PersistentDataType.TAG_CONTAINER,
                container
        );
    }

    @NotNull
    default PersistentDataContainer getEntry(Player player) {
        PersistentDataContainer container = getContainer(player);

        return container.getOrDefault(
                getKey(),
                PersistentDataType.TAG_CONTAINER,
                container.getAdapterContext().newPersistentDataContainer()
        );
    }

    default void saveEntry(Player player, PersistentDataContainer entry) {
        PersistentDataContainer container = getContainer(player);

        container.set(
                getKey(),
                PersistentDataType.TAG_CONTAINER,
                entry
        );

        saveContainer(player, container);
    }

    /**
     * Checks if player has container data in his container.
     * @param player Player that container would be checked
     * @param key Field key
     * @return {@code true} if player has data in PDC
     */
    default boolean hasContainerData(Player player, NamespacedKey key) {
        return getContainer(player).has(key);
    }

    default boolean hasEntryData(Player player, NamespacedKey key) {
        return getEntry(player).has(key);
    }

    default <T, Z> void setContainerData(Player player, NamespacedKey key, PersistentDataType<T, Z> dataType, Z value) {
        PersistentDataContainer container = getContainer(player);
        container.set(key, dataType, value);
        saveContainer(player, container);
    }

    default <T, Z> void setEntryData(Player player, NamespacedKey key, PersistentDataType<T, Z> dataType, Z value) {
        PersistentDataContainer entry = getEntry(player);
        entry.set(key, dataType, value);
        saveEntry(player, entry);
    }

    /**
     * Removes container data from player's PDC
     * @param player Player whose container data would be removed
     * @param key Container field key
     */
    default void removeContainerData(Player player, NamespacedKey key) {
        PersistentDataContainer container = getContainer(player);
        container.remove(key);
        saveContainer(player, container);
    }

    default void removeEntryData(Player player, NamespacedKey key) {
        PersistentDataContainer entry = getEntry(player);
        entry.remove(key);
        saveEntry(player, entry);
    }

    // region Primitives Getters
    default byte getEntryByte(Player player, NamespacedKey key) {
        return getEntry(player).getOrDefault(key, PersistentDataType.BYTE, (byte) 0);
    }

    default short getEntryShort(Player player, NamespacedKey key) {
        return getEntry(player).getOrDefault(key, PersistentDataType.SHORT, (short) 0);
    }

    default int getEntryInt(Player player, NamespacedKey key) {
        return getEntry(player).getOrDefault(key, PersistentDataType.INTEGER, 0);
    }

    default long getEntryLong(Player player, NamespacedKey key) {
        return getEntry(player).getOrDefault(key, PersistentDataType.LONG, 0L);
    }

    default float getEntryFloat(Player player, NamespacedKey key) {
        return getEntry(player).getOrDefault(key, PersistentDataType.FLOAT, 0.0f);
    }

    default double getEntryDouble(Player player, NamespacedKey key) {
        return getEntry(player).getOrDefault(key, PersistentDataType.DOUBLE, 0.0);
    }

    default boolean getEntryBoolean(Player player, NamespacedKey key) {
        return getEntry(player).getOrDefault(key, PersistentDataType.BOOLEAN, false);
    }

    // endregion

    // region Object Getters

    default String getEntryString(Player player, NamespacedKey key) {
        return getEntry(player).get(key, PersistentDataType.STRING);
    }

    default UUID getEntryUUID(Player player, NamespacedKey key) {
        return getEntry(player).get(key, DataType.UUID);
    }
    default ItemStack[] getEntryInventory(Player player, NamespacedKey key) {
        return getEntry(player).getOrDefault(key, DataType.ITEM_STACK_ARRAY, new ItemStack[]{});
    }

    default PersistentDataContainer getEntryTagContainer(Player player, NamespacedKey key) {
        PersistentDataContainer entry = getEntry(player);
        return entry.getOrDefault(key, PersistentDataType.TAG_CONTAINER, entry.getAdapterContext().newPersistentDataContainer());
    }

    // endregion

    // region Primitives Setters
    default void setEntryByte(Player player, NamespacedKey key, byte value) {
        setEntryData(player, key, PersistentDataType.BYTE, value);
    }

    default void setEntryShort(Player player, NamespacedKey key, short value) {
        setEntryData(player, key, PersistentDataType.SHORT, value);
    }

    default void setEntryInt(Player player, NamespacedKey key, int value) {
        setEntryData(player, key, PersistentDataType.INTEGER, value);
    }

    default void setEntryLong(Player player, NamespacedKey key, long value) {
        setEntryData(player, key, PersistentDataType.LONG, value);
    }

    default void setEntryFloat(Player player, NamespacedKey key, float value) {
        setEntryData(player, key, PersistentDataType.FLOAT, value);
    }

    default void setEntryDouble(Player player, NamespacedKey key, double value) {
        setEntryData(player, key, PersistentDataType.DOUBLE, value);
    }

    default void setEntryBoolean(Player player, NamespacedKey key, boolean state) {
        setEntryData(player, key, PersistentDataType.BOOLEAN, state);
    }

    // endregion

    // region Object Setters
    default void setEntryString(Player player, NamespacedKey key, String string) {
        setEntryData(player, key, PersistentDataType.STRING, string);
    }

    default void setEntryUUID(Player player, NamespacedKey key, UUID uuid) {
        setEntryData(player, key, DataType.UUID, uuid);
    }

    default void setEntryInventory(Player player, NamespacedKey key, ItemStack[] items) {
        setEntryData(player, key, DataType.ITEM_STACK_ARRAY, items);
    }

    default void setEntryTagContainer(Player player, NamespacedKey key, PersistentDataContainer tagContainer) {
        setEntryData(player, key, PersistentDataType.TAG_CONTAINER, tagContainer);
    }
    // endregion
}
