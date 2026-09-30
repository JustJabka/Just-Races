package justjabka.justraces.api.events.race;

import justjabka.justraces.api.common.definition.RaceDefinition;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NonNull;

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
            @NonNull Player player,
            @NonNull RaceDefinition oldRace,
            @NonNull RaceDefinition newRace,
            @NonNull Cause cause
    ) {
        super(player);
        this.oldRace = oldRace;
        this.newRace = newRace;
        this.cause = cause;
    }

    /**
     * @return Player's race, that will be changed
     */
    @NonNull
    public RaceDefinition getOldRace() {
        return oldRace;
    }

    /**
     * @return Player's race, that will be set
     */
    @NonNull
    public RaceDefinition getNewRace() {
        return newRace;
    }

    /**
     * @param race Player's new race
     */
    public void setNewRace(@NonNull RaceDefinition race) {
        this.newRace = race;
    }

    /**
     * @return Cause of the race change
     */
    @NonNull
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
