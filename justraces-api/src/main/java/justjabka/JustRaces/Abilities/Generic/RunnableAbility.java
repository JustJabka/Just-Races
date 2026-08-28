package justjabka.JustRaces.Abilities.Generic;

import org.bukkit.scheduler.BukkitRunnable;

public interface RunnableAbility extends ResettableAbility {
    BukkitRunnable createRunnable();
}
