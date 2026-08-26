package justjabka.JustRaces.Abilities.Generic;

import org.bukkit.entity.Player;

public interface BaseDurationAbility extends BaseResettableAbility {
    int getDurationTicks();
    default void onExpire(Player player) {
        resetState(player);
    }
}
