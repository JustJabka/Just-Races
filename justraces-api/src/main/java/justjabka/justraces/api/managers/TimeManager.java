package justjabka.justraces.api.managers;

import org.bukkit.Bukkit;

public final class TimeManager {

    private TimeManager() {}

    public static final long INFINITE_DURATION_STAMP = -1;

    /**
     * Gets gametime from the overworld
     * @return Gametime
     */
    public static long getGameTime() {
        return Bukkit.getWorlds().getFirst().getGameTime();
    }

    public static long getExpireStamp(long ticks) {
        if (ticks == INFINITE_DURATION_STAMP) {
            return INFINITE_DURATION_STAMP;
        }

        return getGameTime() + ticks;
    }

    public static long getRemainingExpireStampTicks(long stamp) {
        if (isExpireStampInfinite(stamp)) {
            return INFINITE_DURATION_STAMP;
        }

        final long remaining = stamp - getGameTime();
        return Math.max(0, remaining);
    }

    public static boolean isExpireStampValid(long stamp) {
        if (isExpireStampInfinite(stamp)) return true;

        return getGameTime() < stamp;
    }

    public static boolean isExpireStampInfinite(long stamp) {
        return stamp == INFINITE_DURATION_STAMP;
    }
}
