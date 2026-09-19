package justjabka.justraces.api.traits.generic;

import org.bukkit.entity.Player;

import java.util.UUID;

public interface ResettableTrait {

    /**
     * The reason why the trait state is being reset.
     * <p>
     * These values are passed by the core plugin when it automatically
     * triggers a reset.
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
         * Player changed their race, invalidating current traits.
         */
        RACE_CHANGE,
        /**
         * Transient Trait expired.
         */
        TRAIT_END,
        /**
         * Any other reason not covered by the causes above
         */
        CUSTOM
    }

    /**
     * Called when the trait is applied to the player
     */
    default void applyState(Player player) {}

    /**
     * Called when the trait is taken from the player
     */
    void resetState(UUID pid, Reason reason);

    default void resetState(Player player, Reason reason) {
        if (player == null) return;
        resetState(player.getUniqueId(), reason);
    }
}
