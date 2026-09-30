package justjabka.justraces.api.events.race;

import justjabka.justraces.api.common.definition.RaceDefinition;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NonNull;

/**
 * Called when player's race is changed
 * @see PlayerRaceChangePreEvent
 */
public class PlayerRaceChangeEvent extends PlayerEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final RaceDefinition oldRace;
    private final RaceDefinition newRace;
    private final Cause cause;

    @ApiStatus.Internal
    public PlayerRaceChangeEvent(
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
     * @return Player's race, that was changed
     */
    @NonNull
    public RaceDefinition getOldRace() {
        return oldRace;
    }

    /**
     * @return Player's race, that was set
     */
    @NonNull
    public RaceDefinition getNewRace() {
        return newRace;
    }

    /**
     * @return Cause of the race change
     */
    @NonNull
    public Cause getCause() {
        return cause;
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
