package justjabka.JustRaces.Abilities.Generic;

import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class BaseValidationAbility extends BaseAbility {
    public boolean isStateValid(Player player) {
        return true;
    }

    public void onDeactivation(Player player) {}

    public void stopTask(UUID pid) {}
}
