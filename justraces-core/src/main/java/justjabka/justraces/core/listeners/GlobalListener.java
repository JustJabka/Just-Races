package justjabka.justraces.core.listeners;

import io.papermc.paper.event.entity.EntityEquipmentChangedEvent;
import justjabka.justraces.api.abilities.generic.ResettableAbility;
import justjabka.justraces.api.managers.AbilityManager;
import justjabka.justraces.api.managers.ArmorManager;
import justjabka.justraces.api.managers.TraitManager;
import justjabka.justraces.api.managers.TransientManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GlobalListener implements Listener {
    // Inventory
    @EventHandler(ignoreCancelled = true)
    public void onArmorChange(EntityEquipmentChangedEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        ArmorManager.updateArmorSet(player);
    }

    // Interactions
    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        AbilityManager.clearAbilitiesStates(player, ResettableAbility.Reason.QUIT);
        TraitManager.endTraits(player);
        // TODO: refresh attributes after rejoin & reload
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        TransientManager.clearTransientContainer(player);
        // TODO: clear abilities cooldown
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();

        AbilityManager.endAbilities(player, ResettableAbility.Reason.DEATH);
        TraitManager.endTraits(player);
    }
}