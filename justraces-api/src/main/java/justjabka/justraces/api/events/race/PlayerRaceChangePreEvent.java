package justjabka.justraces.api.events.race;

import justjabka.justraces.api.definitions.RaceDefinition;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when player's race is changing
 * @see PlayerRaceChangeEvent
 */
public class PlayerRaceChangePreEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final RaceDefinition oldRace;
    private RaceDefinition newRace;
    private final Cause cause;

    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerRaceChangePreEvent(
            @NotNull Player player,
            @NotNull RaceDefinition oldRace,
            @NotNull RaceDefinition newRace,
            @NotNull Cause cause
    ) {
        super(player);
        this.oldRace = oldRace;
        this.newRace = newRace;
        this.cause = cause;
    }

    /**
     * @return Player's race, that will be changed
     */
    @NotNull
    public RaceDefinition getOldRace() {
        return oldRace;
    }

    /**
     * @return Player's race, that will be set
     */
    @NotNull
    public RaceDefinition getNewRace() {
        return newRace;
    }

    /**
     * @param race Player's new race
     */
    public void setNewRace(@NotNull RaceDefinition race) {
        this.newRace = race;
    }

    /**
     * @return Cause of the race change
     */
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
}
