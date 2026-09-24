package justjabka.justraces.api.managers;

import justjabka.justraces.api.JustRacesAPI;
import justjabka.justraces.api.JustRacesRegistries;
import justjabka.justraces.api.abilities.generic.BaseAbility;
import justjabka.justraces.api.abilities.generic.ResettableAbility;
import justjabka.justraces.api.abilities.generic.ValidationAbility;
import justjabka.justraces.api.common.definition.RaceDefinition;
import justjabka.justraces.api.common.entry.AbilityEntry;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.HashSet;
import java.util.Set;

@NullMarked
public final class AbilityManager {

    private AbilityManager() {}

    public static final NamespacedKey ABILITIES_CONTAINER_KEY = new NamespacedKey(JustRacesAPI.NAMESPACE, "abilities");

    //region Registry Related
    public static BaseAbility getByKey(NamespacedKey key) {
        BaseAbility ability = JustRacesRegistries.ABILITIES.get(key);

        if (ability == null) {
            throw new IllegalArgumentException("Unregistered Ability: %s".formatted(key));
        }

        return ability;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public static <T extends BaseAbility> T getByClass(Class<T> abilityClass) {
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
    public static Set<BaseAbility> getAbilitiesForRace(RaceDefinition race) {
        return race.getAbilities();
    }

    /**
     * Returns abilities that this player has
     * @param player Player that abilities will be got
     * @see #getAbilitiesForRace(RaceDefinition)
     * @return Abilities of the player
     */
    public static Set<BaseAbility> getAbilitiesForPlayer(Player player) {
        Set<BaseAbility> allAbilities = new HashSet<>();

        allAbilities.addAll(getAbilitiesForRace(RaceManager.getRace(player)));
        allAbilities.addAll(TransientManager.getTransientAbilities(player).abilities());

        return allAbilities;
    }

    public static Set<AbilityEntry> getAbilityEntriesForRace(RaceDefinition race) {
        return race.getAbilityEntries();
    }

    public static Set<AbilityEntry> getAbilityEntriesForPlayer(Player player) {
        Set<AbilityEntry> allRaceAbilities = new HashSet<>();

        allRaceAbilities.addAll(getAbilityEntriesForRace(RaceManager.getRace(player)));
        allRaceAbilities.addAll(TransientManager.getTransientAbilities(player).entries());

        return allRaceAbilities;
    }
    //endregion

    /**
     * Checks if player has this ability
     * @param player Player
     * @param ability Ability
     * @return {@code true} if player has this ability
     */
    public static boolean playerHasAbility(Player player, BaseAbility ability) {
        return AbilityManager.getAbilitiesForPlayer(player).contains(ability);
    }

    /**
     * Checks if player has this ability
     * @param player Player
     * @param key Key of the ability
     * @return {@code true} if player has this ability
     */
    public static boolean playerHasAbility(Player player, NamespacedKey key) {
        BaseAbility ability = getByKey(key);
        return playerHasAbility(player, ability);
    }

    public static void endAbilities(Player player, ResettableAbility.Reason reason) {
        Set<BaseAbility> abilities = getAbilitiesForPlayer(player);

        abilities.forEach(ability ->
                endAbility(player, ability, reason)
        );
    }

    public static void endAbility(Player player, BaseAbility ability, ResettableAbility.Reason reason) {
        if (shouldResetAbilityCooldown(reason)) ability.resetCooldown(player);
        ability.removeCooldownBar(player);
        clearAbilityStates(player, ability, reason);
    }

    private static boolean shouldResetAbilityCooldown(ResettableAbility.Reason reason) {
        return reason == ResettableAbility.Reason.DEATH || reason == ResettableAbility.Reason.RACE_CHANGE;
    }

    public static void clearAbilitiesStates(Player player, ResettableAbility.Reason reason) {
        Set<BaseAbility> abilities = getAbilitiesForPlayer(player);

        abilities.forEach(ability -> clearAbilityStates(player, ability, reason));
    }

    private static void clearAbilityStates(Player player, BaseAbility ability, ResettableAbility.Reason reason) {
        if (ability instanceof ResettableAbility resettable) resettable.resetState(player, reason);
        else if (ability instanceof ValidationAbility validation) validation.onInvalidated(player);
    }
}
