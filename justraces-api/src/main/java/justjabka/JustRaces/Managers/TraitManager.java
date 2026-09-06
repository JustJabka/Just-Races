package justjabka.JustRaces.Managers;

import justjabka.JustRaces.Definitions.RaceDefinition;
import justjabka.JustRaces.Interfaces.Trait;
import justjabka.JustRaces.JustRacesAPI;
import justjabka.JustRaces.JustRacesRegistries;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class TraitManager {
    public static final NamespacedKey TRAITS_CONTAINER_KEY = new NamespacedKey(JustRacesAPI.NAMESPACE, "traits");

    @Nullable
    public static Trait getByKey(NamespacedKey key) {
        return JustRacesRegistries.TRAITS.get(key);
    }

    /**
     * Returns traits that this race has
     * @param race Race that traits will be got
     * @return Traits of the race
     * @see #getTraitsForRace(RaceDefinition)
     */
    @NotNull
    public static Set<@NotNull Trait> getTraitsForRace(RaceDefinition race) {
        return race.getTraits();
    }

    /**
     * Returns traits that this player has
     * @param player Player that traits will be got
     * @return Traits of the player
     * @see #getTraitsForRace(RaceDefinition)
     */
    @NotNull
    public static Set<@NotNull Trait> getTraitsForPlayer(Player player) {
        return getTraitsForRace(RaceManager.getRace(player));
    }
}
