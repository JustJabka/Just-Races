package justjabka.justraces.api.managers;

import justjabka.justraces.api.JustRacesAPI;
import justjabka.justraces.api.common.TimerBar;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.ShadowColor;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@NullMarked
public final class TimerBarManager {

    private TimerBarManager() {}

    private static final Map<UUID, Map<TimerBar, BossBar>> ACTIVE_BARS = new ConcurrentHashMap<>();

    public static final Key TIMER_BAR_FONT = Key.key(JustRacesAPI.NAMESPACE, "timer_bar");
    public static final Component TIMER_BAR_ICON_OFFSET = Component.text("\uDB00\uDCC6").font(TIMER_BAR_FONT);

    public static void updateBar(Player player, @Nullable TimerBar bar) {
        if (bar == null) return;

        if (!bar.shouldBarDisplay(player)) {
            removeBar(player, bar);
            return;
        }

        final float progress = Math.clamp(bar.getBarProgress(player), BossBar.MIN_PROGRESS, BossBar.MAX_PROGRESS);
        final Component icon = bar.getBarIcon(player)
                .shadowColor(ShadowColor.none())
                .append(TIMER_BAR_ICON_OFFSET);
        final BossBar.Color color = bar.getBarColor(player);

        Map<TimerBar, BossBar> playerBars = getPlayerBars(player);

        BossBar bossbar = playerBars.computeIfAbsent(bar, _ -> {
            BossBar b = BossBar.bossBar(icon, progress, color, BossBar.Overlay.NOTCHED_6);
            player.showBossBar(b);
            return b;
        });

        bossbar.name(icon);
        bossbar.color(color);
        bossbar.progress(progress);
    }

    public static void removeBar(Player player, @Nullable TimerBar bar) {
        if (bar == null) return;

        UUID pid = player.getUniqueId();
        Map<TimerBar, BossBar> playerBars = ACTIVE_BARS.get(pid);
        if (playerBars == null) return;

        BossBar bossBar = playerBars.remove(bar);
        if (bossBar == null) return;

        player.hideBossBar(bossBar);

        if (!playerBars.isEmpty()) return;
        ACTIVE_BARS.remove(pid);
    }

    public static void removeAllBars(Player player) {
        Map<TimerBar, BossBar> playerBars = ACTIVE_BARS.remove(player.getUniqueId());
        if (playerBars == null) return;

        playerBars.values().forEach(player::hideBossBar);
    }

    private static Map<TimerBar, BossBar> getPlayerBars(Player player) {
        return ACTIVE_BARS.computeIfAbsent(player.getUniqueId(), _ -> new ConcurrentHashMap<>());
    }
}
