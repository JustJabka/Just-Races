package justjabka.justraces.api.events.ability;

import justjabka.justraces.api.abilities.AbilityContext;
import justjabka.justraces.api.abilities.generic.BaseAbility;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NonNull;

public class PlayerAbilityTriggerEvent extends PlayerEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final BaseAbility ability;
    private final AbilityContext ctx;

    @ApiStatus.Internal
    public PlayerAbilityTriggerEvent(
            @NonNull Player player,
            @NonNull BaseAbility ability,
            @NonNull AbilityContext ctx
    ) {
        super(player);
        this.ability = ability;
        this.ctx = ctx;
    }

    @NonNull
    public BaseAbility getAbility() {
        return ability;
    }

    @NonNull
    public AbilityContext getAdditionalContext() {
        return ctx;
    }

    @NonNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NonNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
