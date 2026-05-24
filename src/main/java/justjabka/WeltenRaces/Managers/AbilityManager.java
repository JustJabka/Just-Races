package justjabka.WeltenRaces.Managers;

import com.jeff_media.morepersistentdatatypes.DataType;
import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.Instances.RaceInstance;
import justjabka.WeltenRaces.Registries.AbilitiesRegistry;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AbilityManager {
    private static final NamespacedKey ABILITIES_CONTAINER_KEY = new NamespacedKey(WeltenRaces.NAMESPACE, "abilities");
    private static final NamespacedKey ABILITY_VISIBILITY_KEY = new NamespacedKey(WeltenRaces.NAMESPACE, "ability_visibility");

    private static final int activationSlot = 8;

    //region Container Manipulations
    public static PersistentDataContainer getAbilitiesContainer(Player player) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();

        return pdc.getOrDefault(
                ABILITIES_CONTAINER_KEY,
                PersistentDataType.TAG_CONTAINER,
                pdc.getAdapterContext().newPersistentDataContainer()
        );
    }

    public static void saveAbilitiesContainer(Player player, PersistentDataContainer abilities) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        pdc.set(
                ABILITIES_CONTAINER_KEY,
                PersistentDataType.TAG_CONTAINER,
                abilities
        );
    }

    public static void changeAbilitiesVisibility(Player player, boolean status) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        pdc.set(ABILITY_VISIBILITY_KEY, PersistentDataType.BOOLEAN, status);
    }

    public static boolean isAbilitiesVisible(Player player) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        return pdc.getOrDefault(ABILITY_VISIBILITY_KEY, PersistentDataType.BOOLEAN, true);
    }
    //endregion



    //region Registry Related
    @SuppressWarnings("unchecked")
    public static <T extends BaseAbility> T getAbility(Class<T> abilityClass) {
        for (BaseAbility ability : AbilitiesRegistry.getAbilities().values()) {
            if (!abilityClass.isInstance(ability)) continue;
            return (T) ability;
        }

        return null;
    }


    public static Set<BaseAbility> getAbilitiesForRace(RaceInstance race) {
        Set<BaseAbility> raceAbilities = new HashSet<>();

        if (race == null) return raceAbilities;
        if (race.getAbilities() == null) return raceAbilities;

        for (String abilityString : race.getAbilities()) {
            NamespacedKey key = NamespacedKey.fromString(abilityString);
            if (key == null) continue;

            BaseAbility ability = AbilitiesRegistry.getAbilities().get(key);

            if (ability == null) {
                WeltenRaces.LOGGER.warn("Race '{}' requires unknown ability: {}", race.getKey(), abilityString);
                continue;
            }
            raceAbilities.add(ability);
        }

        return raceAbilities;
    }

    //endregion



    //region Getters
    public static boolean isAbilityActive(Player player, NamespacedKey key) {
        return getAbilitiesContainer(player).getOrDefault(key, PersistentDataType.BOOLEAN, false);
    }

    public static int getAbilityValue(Player player, NamespacedKey key) {
        return getAbilitiesContainer(player).getOrDefault(key, PersistentDataType.INTEGER, 0);
    }
    //endregion



    // region Data Manipulation
    /**
     * Checks if player has ability data in his ability container.
     * @param player Player that abilities would be checked
     * @param key Ability key
     * @return {@code true} if player has data in PDC
     * @apiNote Don't confuse with {@link #getAbilitiesForRace(RaceInstance)}
     */
    public static boolean hasAbilityData(Player player, NamespacedKey key) {
        return getAbilitiesContainer(player).has(key);
    }

    private static <T, Z> void setAbilityData(Player player, NamespacedKey key, PersistentDataType<T, Z> dataType, Z value) {
        PersistentDataContainer container = getAbilitiesContainer(player);
        container.set(key, dataType, value);
        saveAbilitiesContainer(player, container);
    }

    public static void removeAbilityData(Player player, NamespacedKey key) {
        PersistentDataContainer abilities = getAbilitiesContainer(player);
        abilities.remove(key);
        saveAbilitiesContainer(player, abilities);
    }
    // endregion



    // region Utils
    public static void setAbilityState(Player player, NamespacedKey key, boolean state) {
        setAbilityData(player, key, PersistentDataType.BOOLEAN, state);
    }

    public static void setAbilityOwner(Player player, NamespacedKey key, UUID uuid) {
        setAbilityData(player, key, DataType.UUID, uuid);
    }

    public static void setAbilityValue(Player player, NamespacedKey key, int value) {
        setAbilityData(player, key, PersistentDataType.INTEGER, value);
    }

    public static int getActivationSlot() {
        return activationSlot;
    }

    public static boolean isActivationSlotSelected(Player player) {
        int hotbarSlot = player.getInventory().getHeldItemSlot();
        return hotbarSlot == activationSlot;
    }
    //endregion
}
