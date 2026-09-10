package justjabka.justraces.api.abilities.generic;

import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Interface for abilities that have a finite duration after activation.
 * <p>
 * Subclasses must define the duration via {@link #getDurationTicks()}
 * and should schedule their own expiration logic (e.g., a {@link org.bukkit.scheduler.BukkitTask} or {@link org.bukkit.scheduler.BukkitScheduler})
 * that calls {@link #onExpire(Player)} when the time runs out.
 * <p>
 * Extends {@link ResettableAbility} so the state can be cleaned up uniformly.
 * Subclasses should override {@link #resetState(UUID, Reason)} to implement
 * their cleanup logic.
 */
public interface DurationAbility extends ResettableAbility {

    /**
     * Gets the duration of the ability in ticks.
     *
     * @return Duration in ticks
     */
    long getDurationTicks();

    /**
     * Convenience wrapper that resets the ability state with {@link Reason#ABILITY_END}.
     * <p>
     * This method is <b>not</b> called automatically by the core.
     * Subclasses are expected to invoke it (or call {@link #resetState(Player, Reason)} directly)
     * from their own scheduled task or logic when the duration expires.
     * <p>
     * For custom cleanup logic, prefer overriding {@link #resetState(UUID, Reason)}
     * rather than this method, since {@code resetState} is the universal reset entry point
     * used by the core and other systems.
     *
     * @param player The player whose ability duration has expired
     * @see #resetState(UUID, Reason)
     * @see Reason#ABILITY_END
     */
    default void onExpire(Player player) {
        resetState(player, Reason.ABILITY_END);
    }
}
