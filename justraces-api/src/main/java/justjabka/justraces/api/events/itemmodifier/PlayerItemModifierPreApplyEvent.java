package justjabka.justraces.api.events.itemmodifier;

import justjabka.justraces.api.itemmodifiers.generic.BaseItemModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NonNull;

public class PlayerItemModifierPreApplyEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final BaseItemModifier modifier;
    private final ItemStack item;

    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerItemModifierPreApplyEvent(
            @NonNull Player player,
            @NonNull BaseItemModifier modifier,
            @NonNull ItemStack item
    ) {
        super(player);
        this.modifier = modifier;
        this.item = item;
    }

    @NonNull
    public BaseItemModifier getModifier() {
        return modifier;
    }

    @NonNull
    public ItemStack getItem() {
        return item;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
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
