package justjabka.WeltenRaces.Listeners;

import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.Types.Race;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class EpiphyteRaceListener implements Listener {
    @EventHandler
    public void onItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();

        if (RaceManager.getRace(player) != Race.EPIPHYTE) return;

        ItemStack consumedItem = event.getItem();
        if (consumedItem.getType() != Material.MOSS_BLOCK) return;

        player.heal(6, EntityRegainHealthEvent.RegainReason.EATING);
    }
}