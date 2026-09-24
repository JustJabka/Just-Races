package justjabka.justraces.api.events.itemmodifier;

import justjabka.justraces.api.itemmodifiers.generic.BaseItemModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class PlayerItemModifierPreApplyEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final BaseItemModifier modifier;
    private final ItemStack item;

    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerItemModifierPreApplyEvent(
            @NotNull Player player,
            @NotNull BaseItemModifier modifier,
            @NotNull ItemStack item
    ) {
        super(player);
        this.modifier = modifier;
        this.item = item;
    }

    @NotNull
    public BaseItemModifier getModifier() {
        return modifier;
    }

    @NotNull
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
