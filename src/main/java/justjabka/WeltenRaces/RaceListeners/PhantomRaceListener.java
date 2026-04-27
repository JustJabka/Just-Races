package justjabka.WeltenRaces.RaceListeners;

import justjabka.WeltenRaces.Configs.Race.PhantomConfig;
import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.Types.Race;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class PhantomRaceListener implements Listener {
    private final PhantomConfig config;

    public PhantomRaceListener(PhantomConfig config) {
        this.config = config;
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();

        if (RaceManager.getRace(player) != Race.PHANTOM) return;
        if (event.getItem().getType() != Material.PHANTOM_MEMBRANE) return;

        player.heal(config.membraneHealAmount, EntityRegainHealthEvent.RegainReason.EATING);
    }
}
