package justjabka.JustRaces.Abilities.Generic;

import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Interface for abilities that need to be reset or cleaned up when
 * certain game events occur (e.g., player quit, death, race change).
 * <p>
 * The core plugin invokes {@link #resetState(UUID, Reason)} automatically
 * when the corresponding event fires (passing the appropriate {@link Reason}).
 * However, it can also be called manually from ability logic.
 * <p>
 * Implementations should perform all necessary state cleanup inside
 * {@link #resetState(UUID, Reason)}.
 */
public interface ResettableAbility {

    /**
     * The reason why the ability state is being reset.
     * <p>
     * These values are passed by the core plugin when it automatically
     * triggers a reset, and can also be used manually.
     */
    enum Reason {
        /**
         * Player disconnected from the server.
         */
        QUIT,
        /**
         Player died.
         */
        DEATH,
        /**
         * Player changed their race, invalidating current abilities.
         */
        RACE_CHANGE,
        /**
         * Ability's natural duration or lifecycle ended.
         */
        ABILITY_END,
        /**
         * Any other reason not covered by the causes above
         */
        CUSTOM
    }

    /**
     * Resets the ability state for the specified player.
     * <p>
     * This is the <b>universal reset entry point</b>. It is called automatically
     * by the core plugin when relevant events occur (e.g., player quit, death,
     * race change), with the appropriate {@link Reason} passed in.
     * It can also be called manually from ability logic.
     * <p>
     * Implementations should perform all necessary cleanup: removing active effects,
     * cancelling scheduled tasks, clearing internal state, etc.
     *
     * @param pid    The unique identifier of the player whose ability is being reset
     * @param reason The reason why the reset is being performed
     * @see Reason
     */
    void resetState(UUID pid, Reason reason);

    /**
     * Resets the ability state for the specified player.
     * <p>
     * Convenience overload that delegates to {@link #resetState(UUID, Reason)}.
     * Does nothing if {@code player} is {@code null}.
     *
     * @param player The player whose ability is being reset
     * @param reason The reason why the reset is being performed
     * @see #resetState(UUID, Reason)
     */
    default void resetState(Player player, Reason reason) {
        if (player == null) return;
        resetState(player.getUniqueId(), reason);
    }
}
