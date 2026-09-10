package justjabka.justraces.api.interfaces;

import com.jeff_media.morepersistentdatatypes.DataType;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface PersistentHolder {
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

    default void saveContainer(Player player, PersistentDataContainer abilities) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        pdc.set(
                getContainerKey(),
                PersistentDataType.TAG_CONTAINER,
                abilities
        );
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

    default <T, Z> void setContainerData(Player player, NamespacedKey key, PersistentDataType<T, Z> dataType, Z value) {
        PersistentDataContainer container = getContainer(player);
        container.set(key, dataType, value);
        saveContainer(player, container);
    }

    /**
     * Removes container data from player's PDC
     * @param player Player whose container data would be removed
     * @param key Container field key
     */
    default void removeContainerData(Player player, NamespacedKey key) {
        PersistentDataContainer abilities = getContainer(player);
        abilities.remove(key);
        saveContainer(player, abilities);
    }

    // region Primitives Getters
    default byte getContainerByte(Player player, NamespacedKey key) {
        return getContainer(player).getOrDefault(key, PersistentDataType.BYTE, (byte) 0);
    }

    default short getContainerShort(Player player, NamespacedKey key) {
        return getContainer(player).getOrDefault(key, PersistentDataType.SHORT, (short) 0);
    }

    default int getContainerInt(Player player, NamespacedKey key) {
        return getContainer(player).getOrDefault(key, PersistentDataType.INTEGER, 0);
    }

    default long getContainerLong(Player player, NamespacedKey key) {
        return getContainer(player).getOrDefault(key, PersistentDataType.LONG, 0L);
    }

    default float getContainerFloat(Player player, NamespacedKey key) {
        return getContainer(player).getOrDefault(key, PersistentDataType.FLOAT, 0.0f);
    }

    default double getContainerDouble(Player player, NamespacedKey key) {
        return getContainer(player).getOrDefault(key, PersistentDataType.DOUBLE, 0.0);
    }

    default boolean getContainerBoolean(Player player, NamespacedKey key) {
        return getContainer(player).getOrDefault(key, PersistentDataType.BOOLEAN, false);
    }

    // endregion

    // region Object Getters

    default String getContainerString(Player player, NamespacedKey key) {
        return getContainer(player).get(key, PersistentDataType.STRING);
    }

    default UUID getContainerUUID(Player player, NamespacedKey key) {
        return getContainer(player).get(key, DataType.UUID);
    }
    default ItemStack[] getContainerInventory(Player player, NamespacedKey key) {
        return getContainer(player).getOrDefault(key, DataType.ITEM_STACK_ARRAY, new ItemStack[]{});
    }

    default PersistentDataContainer getContainerTagContainer(Player player, NamespacedKey key) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        return getContainer(player).getOrDefault(key, PersistentDataType.TAG_CONTAINER, pdc.getAdapterContext().newPersistentDataContainer());
    }

    // endregion

    // region Primitives Setters
    default void setContainerByte(Player player, NamespacedKey key, byte value) {
        setContainerData(player, key, PersistentDataType.BYTE, value);
    }

    default void setContainerShort(Player player, NamespacedKey key, short value) {
        setContainerData(player, key, PersistentDataType.SHORT, value);
    }

    default void setContainerInt(Player player, NamespacedKey key, int value) {
        setContainerData(player, key, PersistentDataType.INTEGER, value);
    }

    default void setContainerLong(Player player, NamespacedKey key, long value) {
        setContainerData(player, key, PersistentDataType.LONG, value);
    }

    default void setContainerFloat(Player player, NamespacedKey key, float value) {
        setContainerData(player, key, PersistentDataType.FLOAT, value);
    }

    default void setContainerDouble(Player player, NamespacedKey key, double value) {
        setContainerData(player, key, PersistentDataType.DOUBLE, value);
    }

    default void setContainerBoolean(Player player, NamespacedKey key, boolean state) {
        setContainerData(player, key, PersistentDataType.BOOLEAN, state);
    }

    // endregion

    // region Object Setters
    default void setContainerString(Player player, NamespacedKey key, String string) {
        setContainerData(player, key, PersistentDataType.STRING, string);
    }

    default void setContainerUUID(Player player, NamespacedKey key, UUID uuid) {
        setContainerData(player, key, DataType.UUID, uuid);
    }

    default void setContainerInventory(Player player, NamespacedKey key, ItemStack[] items) {
        setContainerData(player, key, DataType.ITEM_STACK_ARRAY, items);
    }

    default void setContainerTagContainer(Player player, NamespacedKey key, PersistentDataContainer tagContainer) {
        setContainerData(player, key, PersistentDataType.TAG_CONTAINER, tagContainer);
    }
    // endregion
}
