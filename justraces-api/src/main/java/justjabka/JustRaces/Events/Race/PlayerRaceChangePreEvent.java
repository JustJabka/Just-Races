package justjabka.JustRaces.Events.Race;

import justjabka.JustRaces.Instances.RaceInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerRaceChangePreEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final RaceInstance oldRace;
    private final RaceInstance newRace;
    private final Cause cause;

    private boolean cancelled;

    public PlayerRaceChangePreEvent(
            @NotNull Player player,
            @NotNull RaceInstance oldRace,
            @NotNull RaceInstance newRace,
            @NotNull Cause cause
    ) {
        super(player);
        this.oldRace = oldRace;
        this.newRace = newRace;
        this.cause = cause;
    }

    @NotNull
    public RaceInstance getOldRace() {
        return oldRace;
    }

    @NotNull
    public RaceInstance getNewRace() {
        return newRace;
    }

    @NotNull
    public Cause getCause() {
        return cause;
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

    public enum Cause {
        COMMAND,
        DIALOG,
        CUSTOM
    }
}
