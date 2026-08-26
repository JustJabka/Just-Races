package justjabka.JustRaces.Abilities.Generic;

import org.bukkit.scheduler.BukkitRunnable;

public interface BaseRunnableAbility extends BaseResettableAbility {
    BukkitRunnable createRunnable();
}
