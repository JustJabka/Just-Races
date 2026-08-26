package justjabka.JustRaces.Abilities.Generic;

import org.bukkit.entity.Player;

import java.util.UUID;

public interface BaseResettableAbility {
    void resetState(UUID pid);

    default void resetState(Player player) {
        if (player == null) return;
        resetState(player.getUniqueId());
    }
}
