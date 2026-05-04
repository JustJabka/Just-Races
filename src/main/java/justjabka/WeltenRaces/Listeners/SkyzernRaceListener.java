package justjabka.WeltenRaces.Listeners;

import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.Types.Race;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SkyzernRaceListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        if (RaceManager.getRace(player) != Race.SKYZERN) return;

        if (event.isSneaking()) {
            PotionEffect onSneakEffect = new PotionEffect(
                    PotionEffectType.SLOW_FALLING,
                    PotionEffect.INFINITE_DURATION,
                    0,
                    false,
                    false,
                    false
            );

            player.addPotionEffect(onSneakEffect);
        } else {
            PotionEffect currentEffect = player.getPotionEffect(PotionEffectType.SLOW_FALLING);

            if (currentEffect == null) return;
            if (!currentEffect.isInfinite()) return;

            player.removePotionEffect(PotionEffectType.SLOW_FALLING);
        }
    }
}