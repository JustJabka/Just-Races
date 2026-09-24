package justjabka.justraces.api.events.ability;

import justjabka.justraces.api.abilities.AbilityContext;
import justjabka.justraces.api.abilities.generic.BaseAbility;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class PlayerAbilityTriggerPreEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final BaseAbility ability;
    private final AbilityContext ctx;

    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerAbilityTriggerPreEvent(
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

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
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
