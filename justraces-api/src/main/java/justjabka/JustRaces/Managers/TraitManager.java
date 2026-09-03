package justjabka.JustRaces.Managers;

import justjabka.JustRaces.Definitions.RaceDefinition;
import justjabka.JustRaces.Interfaces.Trait;
import justjabka.JustRaces.JustRacesRegistries;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class TraitManager {

    @Nullable
    public static Trait getByKey(NamespacedKey key) {
        return JustRacesRegistries.TRAITS.get(key);
    }

    /**
     * Returns traits that this race has
     * @param race Race that traits will be got
     * @return Traits of the race
     */
    @NotNull
    public static Set<@NotNull Trait> getTraitsForRace(RaceDefinition race) {
        return race.getTraits();
    }

    /**
     * Returns traits that this player has
     * @param player Player that traits will be got
     * @see TraitManager#getTraitsForRace(RaceDefinition)
     * @return Traits of the player
     */
    @NotNull
    public static Set<@NotNull Trait> getTraitsForPlayer(Player player) {
        return getTraitsForRace(RaceManager.getRace(player));
    }
}
