package justjabka.JustRaces.Abilities.Generic;

import justjabka.JustRaces.Managers.AbilityManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Base ability class for abilities that can be toggled on and off.
 * <p>
 * Subclasses should implement {@link #onToggle(Player, boolean)} to handle custom logic when the state changes.
 */
public abstract class BaseTogglableAbility extends BaseValidationAbility {

    @Override
    public Component getAbilityDisplay(Player player) {
        boolean isActive = AbilityManager.isAbilityActive(player, getKey());

        TextColor displayColor = isActive ? abilityPrimaryColor : abilitySecondaryColor;

        return getDisplayName().color(displayColor).decorate(TextDecoration.UNDERLINED);
    }

    /**
     * Toggles the ability's state for the given player.
     * <p>
     * This method automatically calls {@link #onToggle(Player, boolean)} after changing the state.
     * @param player The player whose ability state is being toggled.
     */
    public void toggle(Player player) {
        boolean currentState = AbilityManager.isAbilityActive(player, getKey());
        boolean newState = !currentState;

        if (newState) {
            enable(player);
        } else {
            disable(player);
        }

        onToggle(player, newState);
    }

    /**
     * Enables the ability for the given player.
     * <p>
     * This method sets the ability's state to active in the {@link AbilityManager}.
     * Subclasses should override {@link #onToggle(Player, boolean)} to implement custom activation logic.
     *
     * @param player The player for whom the ability is being enabled.
     */
    public void enable(Player player) {
        AbilityManager.setAbilityState(player, getKey(), true);
    }

    /**
     * Disables the ability for the given player.
     * <p>
     * This method sets the ability's state to inactive in the {@link AbilityManager}
     * and stops any associated tasks by calling {@link #stopTask(UUID)}.
     *
     * @param player The player for whom the ability is being disabled.
     */
    public void disable(Player player) {
        AbilityManager.setAbilityState(player, getKey(), false);
        stopTask(player.getUniqueId());
    }

    /**
     * Called when the ability's state is toggled.
     * <p>
     * This method is called after {@link #enable(Player)} or {@link #disable(Player)} is invoked.
     * Subclasses should override this method to implement custom logic when the ability is turned on or off.
     * @param player The player whose ability state was toggled.
     * @param state  The new state of the ability ({@code true} if enabled, {@code false} if disabled).
     */
    public void onToggle(Player player, boolean state) {}
}