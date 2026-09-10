package justjabka.justraces.api.managers;

import org.bukkit.Bukkit;

public class TimeManager {

    /**
     * Gets gametime from the overworld
     * @return Gametime
     */
    public static long getGameTime() {
        return Bukkit.getWorlds().getFirst().getGameTime();
    }

    public static long getExpireStamp(long ticks) {
        return getGameTime() + ticks;
    }

    public static long getRemainingExpireStampTicks(long stamp) {
        final long remaining = stamp - getGameTime();
        return Math.max(0, remaining);
    }

    public static boolean isExpireStampValid(long stamp) {
        return getGameTime() < stamp;
    }
}
