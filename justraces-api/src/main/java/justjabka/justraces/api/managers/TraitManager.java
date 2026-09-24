package justjabka.justraces.api.managers;

import justjabka.justraces.api.JustRacesAPI;
import justjabka.justraces.api.JustRacesRegistries;
import justjabka.justraces.api.common.definition.RaceDefinition;
import justjabka.justraces.api.traits.generic.ResettableTrait;
import justjabka.justraces.api.traits.generic.Trait;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.HashSet;
import java.util.Set;

@NullMarked
public final class TraitManager {

    private TraitManager() {}

    public static final NamespacedKey TRAITS_CONTAINER_KEY = new NamespacedKey(JustRacesAPI.NAMESPACE, "traits");

    public static Trait getByKey(NamespacedKey key) {
        Trait trait = JustRacesRegistries.TRAITS.get(key);

        if (trait == null) {
            throw new IllegalArgumentException("Unregistered Trait: %s".formatted(key));
        }

        return trait;
    }

    /**
     * Returns traits that this race has
     * @param race Race that traits will be got
     * @return Traits of the race
     * @see #getTraitsForRace(RaceDefinition)
     */
    public static Set<Trait> getTraitsForRace(RaceDefinition race) {
        return race.getTraits();
    }

    /**
     * Returns traits that this player has
     * @param player Player that traits will be got
     * @return Traits of the player
     * @see #getTraitsForRace(RaceDefinition)
     */
    public static Set<Trait> getTraitsForPlayer(Player player) {
        Set<Trait> allTraits = new HashSet<>();

        allTraits.addAll(getTraitsForRace(RaceManager.getRace(player)));
        allTraits.addAll(TransientManager.getTransientTraits(player));

        return allTraits;
    }

    /**
     * Checks if player has this trait
     * @param player Player
     * @param trait Trait
     * @return {@code true} if player has this trait
     */
    public static boolean playerHasTrait(Player player, Trait trait) {
        return TraitManager.getTraitsForPlayer(player).contains(trait);
    }

    /**
     * Checks if player has this trait
     * @param player Player
     * @param key Key of the trait
     * @return {@code true} if player has this trait
     */
    public static boolean playerHasTrait(Player player, NamespacedKey key) {
        return playerHasTrait(player, getByKey(key));
    }

    public static void startTraits(Player player) {
        Set<Trait> traits = getTraitsForRace(RaceManager.getRace(player));

        traits.forEach(trait ->
                startTrait(player, trait)
        );
    }

    public static void startTrait(Player player, Trait trait) {
        if (!(trait instanceof ResettableTrait resettable)) return;
        resettable.applyState(player);
    }

    public static void endTraits(Player player, ResettableTrait.Reason reason) {
        Set<Trait> traits = getTraitsForPlayer(player);

        traits.forEach(trait ->
                endTrait(player, trait, reason)
        );
    }

    public static void endTrait(Player player, Trait trait, ResettableTrait.Reason reason) {
        if (!(trait instanceof ResettableTrait resettable)) return;
        resettable.resetState(player, reason);
    }
}
