package justjabka.JustRaces.Abilities.Generic;

import org.bukkit.entity.Player;

public interface DurationAbility extends ResettableAbility {
    int getDurationTicks();
    default void onExpire(Player player) {
        resetState(player, Reason.ABILITY_END);
    }
}
