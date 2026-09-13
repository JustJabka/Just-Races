package justjabka.justraces.api.traits.generic;

import org.bukkit.entity.Player;

import java.util.UUID;

public interface ResettableTrait {

    default void applyState(Player player) {};
    void resetState(UUID pid);

    default void resetState(Player player) {
        if (player == null) return;
        resetState(player.getUniqueId());
    }
}
