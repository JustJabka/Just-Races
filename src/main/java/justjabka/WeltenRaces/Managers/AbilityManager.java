package justjabka.WeltenRaces.Managers;

import com.jeff_media.morepersistentdatatypes.DataType;
import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.Types.Race;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Set;
import java.util.UUID;

import static justjabka.WeltenRaces.Registries.AbilitiesRegistry.RACE_ABILITIES;

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

    public static void updateAbilities(Player player, PersistentDataContainer abilities) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        pdc.set(
                ABILITIES_CONTAINER_KEY,
                PersistentDataType.TAG_CONTAINER,
                abilities
        );
    }

    public static Set<BaseAbility> getAbilitiesForRace(Race race) {
        return RACE_ABILITIES.getOrDefault(race, Set.of());
    }

    public static boolean isAbilityActive(Player player, NamespacedKey key) {
        return AbilityManager.getAbilities(player).getOrDefault(key, PersistentDataType.BOOLEAN, false);
    }

    public static boolean hasAbility(Player player, NamespacedKey key) {
        return AbilityManager.getAbilities(player).has(key);
    }

    public static void removeAbility(Player player, NamespacedKey key) {
        PersistentDataContainer abilities = AbilityManager.getAbilities(player);
        abilities.remove(key);
        AbilityManager.updateAbilities(player, abilities);
    }

    public static void changeAbilityState(Player player, NamespacedKey key, boolean state) {
        PersistentDataContainer abilities = AbilityManager.getAbilities(player);
        abilities.set(key, PersistentDataType.BOOLEAN, state);
        AbilityManager.updateAbilities(player, abilities);
    }

    public static void changeAbilityOwner(Player player, NamespacedKey key, UUID uuid) {
        PersistentDataContainer abilities = AbilityManager.getAbilities(player);
        abilities.set(key, DataType.UUID, uuid);
        AbilityManager.updateAbilities(player, abilities);
    }

    public static int getActivationSlot() {
        return 8;
    }

    public static boolean hasActivationSlotSelected(Player player) {
        int hotbarSlot = player.getInventory().getHeldItemSlot();
        return hotbarSlot == getActivationSlot();
    }
}
