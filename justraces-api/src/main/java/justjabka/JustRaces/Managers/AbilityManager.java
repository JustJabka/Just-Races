package justjabka.JustRaces.Managers;

import com.jeff_media.morepersistentdatatypes.DataType;
import justjabka.JustRaces.Abilities.Generic.BaseAbility;
import justjabka.JustRaces.Abilities.Generic.ResettableAbility;
import justjabka.JustRaces.Abilities.Generic.ValidationAbility;
import justjabka.JustRaces.Definitions.RaceDefinition;
import justjabka.JustRaces.JustRacesAPI;
import justjabka.JustRaces.JustRacesRegistries;
import justjabka.JustRaces.Types.AbilityBinding;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

public class AbilityManager {
    private static final NamespacedKey ABILITIES_CONTAINER_KEY = new NamespacedKey(JustRacesAPI.NAMESPACE, "abilities");

    //region Container Manipulations
    @NotNull
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

    //endregion



    //region Registry Related
    @Nullable
    public static BaseAbility getAbilityByKey(NamespacedKey key) {
        return JustRacesRegistries.ABILITIES.get(key);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public static <T extends BaseAbility> T getAbility(Class<T> abilityClass) {
        for (BaseAbility ability : JustRacesRegistries.ABILITIES.values()) {
            if (!abilityClass.isInstance(ability)) continue;
            return (T) ability;
        }

        return null;
    }

    /**
     * Returns abilities that this race has
     * @param race Race that abilities will be got
     * @return Abilities of the race
     */
    @NotNull
    public static Set<@NotNull BaseAbility> getAbilitiesForRace(RaceDefinition race) {
        return race.getAbilities();
    }

    /**
     * Returns abilities that this player has
     * @param player Player that abilities will be got
     * @see AbilityManager#getAbilitiesForRace(RaceDefinition)
     * @return Abilities of the player
     */
    @NotNull
    public static Set<@NotNull BaseAbility> getAbilitiesForPlayer(Player player) {
        return getAbilitiesForRace(RaceManager.getRace(player));
    }

    @NotNull
    public static Set<@NotNull AbilityBinding> getAbilitiesBindingsForRace(RaceDefinition race) {
        return race.getAbilitiesBindings();
    }

    @NotNull
    public static Set<@NotNull AbilityBinding> getAbilitiesBindingsForPlayer(Player player) {
        return getAbilitiesBindingsForRace(RaceManager.getRace(player));
    }

    //endregion



    //region Getters
    public static boolean isAbilityActive(Player player, NamespacedKey key) {
        return getAbilitiesContainer(player).getOrDefault(key, PersistentDataType.BOOLEAN, false);
    }

    public static int getAbilityValue(Player player, NamespacedKey key) {
        return getAbilitiesContainer(player).getOrDefault(key, PersistentDataType.INTEGER, 0);
    }

    public static UUID getAbilityOwner(Player player, NamespacedKey key) {
        return getAbilitiesContainer(player).get(key, DataType.UUID);
    }

    public static String getAbilityString(Player player, NamespacedKey key) {
        return getAbilitiesContainer(player).get(key, DataType.STRING);
    }
    //endregion


    public static void endAbilities(Player player, ResettableAbility.Reason reason) {
        Set<BaseAbility> abilities = getAbilitiesForPlayer(player);

        abilities.forEach(ability -> {
            ability.resetCooldown(player);
            clearAbilityStates(player, ability, reason);
        });
    }

    public static void clearAbilitiesStates(Player player, ResettableAbility.Reason reason) {
        Set<BaseAbility> abilities = getAbilitiesForPlayer(player);

        abilities.forEach(ability -> clearAbilityStates(player, ability, reason));
    }

    private static void clearAbilityStates(Player player, BaseAbility ability, ResettableAbility.Reason reason) {
        if (ability instanceof ResettableAbility resettable) resettable.resetState(player, reason);
        else if (ability instanceof ValidationAbility validation) validation.onInvalidated(player);
    }


    // region Data Manipulation
    /**
     * Checks if player has ability data in his ability container.
     * @param player Player that abilities would be checked
     * @param key Ability key
     * @return {@code true} if player has data in PDC
     * @apiNote Don't confuse with {@link #getAbilitiesForRace(RaceDefinition)}
     */
    public static boolean hasAbilityData(Player player, NamespacedKey key) {
        return getAbilitiesContainer(player).has(key);
    }

    public static <T, Z> void setAbilityData(Player player, NamespacedKey key, PersistentDataType<T, Z> dataType, Z value) {
        PersistentDataContainer container = getAbilitiesContainer(player);
        container.set(key, dataType, value);
        saveAbilitiesContainer(player, container);
    }

    /**
     * Removes ability data from player's PDC
     * @param player Player whose ability data would be removed
     * @param key Ability key
     */
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

    public static void setAbilityString(Player player, NamespacedKey key, String string) {
        setAbilityData(player, key, PersistentDataType.STRING, string);
    }
    //endregion
}
