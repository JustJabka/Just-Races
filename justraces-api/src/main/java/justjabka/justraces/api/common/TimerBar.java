package justjabka.justraces.api.common;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import static justjabka.justraces.api.managers.TimerBarManager.TIMER_BAR_FONT;

public interface TimerBar {

    default BossBar.Color getBarColor(Player player) {
        return BossBar.Color.WHITE;
    }

    default Component getBarIcon(Player player) {
        return Component.text("\uE000").font(TIMER_BAR_FONT);
    }

    float getBarProgress(Player player);
    boolean shouldBarDisplay(Player player);
}
