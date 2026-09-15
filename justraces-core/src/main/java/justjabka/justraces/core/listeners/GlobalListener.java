package justjabka.justraces.core.listeners;

import io.papermc.paper.event.entity.EntityEquipmentChangedEvent;
import justjabka.justraces.api.abilities.generic.ResettableAbility;
import justjabka.justraces.api.managers.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GlobalListener implements Listener {
    // Inventory
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onArmorChange(EntityEquipmentChangedEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        ArmorManager.updateArmorSet(player);
    }

    // Interactions
    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // TODO: fix cooldown reset exploit
        // TODO: fix cooldown memory leak
        RaceManager.reloadRace(player);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        endEverything(player, ResettableAbility.Reason.QUIT);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();

        endEverything(player, ResettableAbility.Reason.DEATH);
    }

    private static void endEverything(Player player, ResettableAbility.Reason death) {
        AbilityManager.endAbilities(player, death);
        TraitManager.endTraits(player);
        TransientManager.resetTransientContainer(player);
    }
}