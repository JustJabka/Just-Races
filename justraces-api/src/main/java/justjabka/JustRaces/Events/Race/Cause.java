package justjabka.JustRaces.Events.Race;

public enum Cause {
    /**
     * {@code /setrace} command
     */
    COMMAND,
    /**
     * {@code /selectrace} command
     */
    DIALOG,
    /**
     * {@code /justraces reload} command
     */
    RELOAD,
    /**
     * Any other cause not covered by the causes above
     */
    CUSTOM
}
