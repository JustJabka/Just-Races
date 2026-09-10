package justjabka.justraces.api.managers;

import justjabka.justraces.api.abilities.generic.BaseAbility;
import justjabka.justraces.api.abilities.generic.ResettableAbility;
import justjabka.justraces.api.abilities.generic.ValidationAbility;
import justjabka.justraces.api.definitions.RaceDefinition;
import justjabka.justraces.api.JustRacesAPI;
import justjabka.justraces.api.JustRacesRegistries;
import justjabka.justraces.api.types.AbilityBinding;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class AbilityManager {
    public static final NamespacedKey ABILITIES_CONTAINER_KEY = new NamespacedKey(JustRacesAPI.NAMESPACE, "abilities");

    //region Registry Related
    @Nullable
    public static BaseAbility getByKey(NamespacedKey key) {
        return JustRacesRegistries.ABILITIES.get(key);
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
    @NotNull
    public static Set<@NotNull BaseAbility> getAbilitiesForRace(RaceDefinition race) {
        return race.getAbilities();
    }

    /**
     * Returns abilities that this player has
     * @param player Player that abilities will be got
     * @see #getAbilitiesForRace(RaceDefinition)
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

    public static void endAbilities(Player player, ResettableAbility.Reason reason) {
        Set<BaseAbility> abilities = getAbilitiesForPlayer(player);

        abilities.forEach(ability -> {
            ability.resetCooldown(player);
            ability.removeCooldownBar(player);
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
}
