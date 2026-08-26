package justjabka.JustRaces.Abilities.Generic;

import org.bukkit.entity.Player;

/**
 * Base ability class for abilities that have a toggleable state and require validation.
 * <p>
 * It is intended to be subclassed by abilities that need to maintain a persistent state
 * (e.g., passive buffs, continuous effects) rather than instant activation.
 */
public interface BaseValidationAbility {

    /**
     * Checks if the current state of the ability is valid for the given player.
     * <p>
     * This method is typically used to determine if the ability should remain active
     * or if it should be deactivated due to external conditions (e.g., player death,
     * race change, or environmental factors).
     * @param player The player whose ability state is being validated.
     * @return {@code true} if the ability state is valid and should remain active;
     *         {@code false} if the ability should be deactivated.
     */
    default boolean isStateValid(Player player) {
        return true;
    }

    /**
     * Called when the ability state is invalid.
     * <p>
     * This method allows subclasses to perform cleanup actions or trigger events
     * when the ability is turned off. It is called automatically when the ability
     * is toggled off or when {@link #isStateValid(Player)} returns {@code false}.
     * @param player The player from whom the ability was deactivated.
     */
    void onInvalidated(Player player);
}
