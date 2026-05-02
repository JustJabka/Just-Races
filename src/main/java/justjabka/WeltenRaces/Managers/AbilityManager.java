package justjabka.WeltenRaces.Managers;

import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class AbilityManager {
    public static final NamespacedKey ABILITIES_CONTAINER_KEY = new NamespacedKey(WeltenRaces.NAMESPACE, "abilities");

    public static PersistentDataContainer getAbilities(Player player) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();

        return pdc.getOrDefault(
                ABILITIES_CONTAINER_KEY,
                PersistentDataType.TAG_CONTAINER,
                pdc.getAdapterContext().newPersistentDataContainer()
        );
    }

    public static boolean isAbilityActive(Player player, NamespacedKey key) {
        return AbilityManager.getAbilities(player).getOrDefault(key, PersistentDataType.BOOLEAN, false);
    }

    public static void updateAbilities(Player player, PersistentDataContainer abilities) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        pdc.set(
                ABILITIES_CONTAINER_KEY,
                PersistentDataType.TAG_CONTAINER,
                abilities
        );
    }

    public static void changeAbilityState(Player player, NamespacedKey key, boolean state) {
        PersistentDataContainer abilities = AbilityManager.getAbilities(player);
        abilities.set(key, PersistentDataType.BOOLEAN, state);
        AbilityManager.updateAbilities(player, abilities);
    }

    public static boolean hasActivationSlotSelected(Player player) {
        int hotbarSlot = player.getInventory().getHeldItemSlot();
        return hotbarSlot == 8;
    }
}
