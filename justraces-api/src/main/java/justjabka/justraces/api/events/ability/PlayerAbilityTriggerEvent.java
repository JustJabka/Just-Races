package justjabka.justraces.api.events.ability;

import justjabka.justraces.api.abilities.AbilityContext;
import justjabka.justraces.api.abilities.generic.BaseAbility;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class PlayerAbilityTriggerEvent extends PlayerEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final BaseAbility ability;
    private final AbilityContext ctx;

    @ApiStatus.Internal
    public PlayerAbilityTriggerEvent(
            @NotNull Player player,
            @NotNull BaseAbility ability,
            @NotNull AbilityContext ctx
    ) {
        super(player);
        this.ability = ability;
        this.ctx = ctx;
    }

    @NotNull
    public BaseAbility getAbility() {
        return ability;
    }

    @NotNull
    public AbilityContext getAdditionalContext() {
        return ctx;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
