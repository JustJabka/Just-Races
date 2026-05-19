package justjabka.WeltenRaces.Abilities.Generic;

import justjabka.WeltenRaces.Managers.AbilityManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;

public abstract class BaseTogglableAbility extends BaseValidationAbility {
    @Override
    public Component getAbilityDisplay(Player player) {
        boolean isActive = AbilityManager.isAbilityActive(player, getKey());

        TextColor displayColor = isActive ? abilityPrimaryColor : abilitySecondaryColor;

        return getDisplayName().color(displayColor).decorate(TextDecoration.UNDERLINED);
    }

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

    public void enable(Player player) {
        AbilityManager.setAbilityState(player, getKey(), true);
    }

    public void disable(Player player) {
        AbilityManager.setAbilityState(player, getKey(), false);
        stopTask(player.getUniqueId());
    }

    public void onToggle(Player player, boolean state) {}
}