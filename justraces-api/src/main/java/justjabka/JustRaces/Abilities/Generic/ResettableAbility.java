package justjabka.JustRaces.Abilities.Generic;

import org.bukkit.entity.Player;

import java.util.UUID;

public interface ResettableAbility {

    enum Reason {
        QUIT,
        DEATH,
        RACE_CHANGE,
        ABILITY_END,
        CUSTOM
    }

    void resetState(UUID pid, Reason reason);

    default void resetState(Player player, Reason reason) {
        if (player == null) return;
        resetState(player.getUniqueId(), reason);
    }
}
