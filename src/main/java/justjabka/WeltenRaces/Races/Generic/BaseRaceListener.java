package justjabka.WeltenRaces.Races.Generic;

import io.papermc.paper.event.entity.EntityEquipmentChangedEvent;
import justjabka.WeltenRaces.Managers.ArmorManager;
import justjabka.WeltenRaces.Managers.ItemManager;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class BaseRaceListener implements Listener {
    @EventHandler
    public void onArmorChange(EntityEquipmentChangedEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        ArmorManager.updateArmorSet(player);
    }

    @EventHandler
    public void onPickupItem(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        ItemStack item = event.getItem().getItemStack();
        ItemManager.tryApply(player, item);

        WeltenRaces.LOGGER.info("Picked item");
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        ItemManager.tryApply((Player) event.getWhoClicked(), event.getCurrentItem());
        ItemManager.tryApply((Player) event.getWhoClicked(), event.getCursor());

        WeltenRaces.LOGGER.info("Inventory clicked");
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        ItemManager.tryUndo(item);

        WeltenRaces.LOGGER.info("Dropped item");
    }
}