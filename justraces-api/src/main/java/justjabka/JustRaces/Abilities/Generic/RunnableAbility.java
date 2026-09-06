package justjabka.JustRaces.Abilities.Generic;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * Interface for abilities that run a recurring scheduled task for the player.
 * <p>
 * Subclasses provide the {@link BukkitRunnable} factory method and are responsible
 * for starting it upon activation and cancelling it inside {@link #resetState(UUID, Reason)}.
 * <p>
 * Extends {@link ResettableAbility} to ensure the runnable is canceled on cleanup.
 */
public interface RunnableAbility extends ResettableAbility {

    /**
     * Creates the {@link BukkitRunnable} that will be executed for the given player.
     * <p>
     * This method is <b>not</b> called automatically by the core.
     * The ability implementation is responsible for calling this method,
     * starting the returned runnable, and keeping a reference to cancel it
     * inside {@link #resetState(UUID, Reason)}.
     * <p>
     * The returned runnable should encapsulate the periodic logic of the ability
     * (e.g., applying effects, ticking a timer).
     *
     * @param player The player for whom the runnable is being created
     * @return A new instance of the ability's scheduled task
     * @see #resetState(UUID, Reason)
     */
    BukkitRunnable createRunnable(Player player);
}
