package justjabka.JustRaces.Events.Race;

import justjabka.JustRaces.Instances.RaceInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when player's race is changed
 * @see PlayerRaceChangePreEvent
 */
public class PlayerRaceChangeEvent extends PlayerEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final RaceInstance oldRace;
    private final RaceInstance newRace;
    private final Cause cause;

    @ApiStatus.Internal
    public PlayerRaceChangeEvent(
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

    /**
     * @return Player's race, that was changed
     */
    @NotNull
    public RaceInstance getOldRace() {
        return oldRace;
    }

    /**
     * @return Player's race, that was set
     */
    @NotNull
    public RaceInstance getNewRace() {
        return newRace;
    }

    /**
     * @return Cause of the race change
     */
    @NotNull
    public Cause getCause() {
        return cause;
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
