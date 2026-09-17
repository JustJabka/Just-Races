package justjabka.justraces.api.traits.generic;

import org.bukkit.entity.Player;

import java.util.UUID;

public interface ResettableTrait {
    /**
     * Called when the trait is applied to the player
     */
    default void applyState(Player player) {}

    /**
     * Called when the trait is taken from the player
     */
    void resetState(UUID pid);

    default void resetState(Player player) {
        if (player == null) return;
        resetState(player.getUniqueId());
    }
}
