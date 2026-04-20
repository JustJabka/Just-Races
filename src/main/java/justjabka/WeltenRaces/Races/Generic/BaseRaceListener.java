package justjabka.WeltenRaces.Races.Generic;

import io.papermc.paper.event.entity.EntityEquipmentChangedEvent;
import justjabka.WeltenRaces.Managers.ArmorManager;
import justjabka.WeltenRaces.Managers.ItemManager;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import static justjabka.WeltenRaces.Managers.ItemManager.refreshModifiers;

public class BaseRaceListener implements Listener {
    @EventHandler
    public void onArmorChange(EntityEquipmentChangedEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        ArmorManager.updateArmorSet(player);
    }

    @EventHandler
    public void onInventoryAction(InventoryClickEvent event) {
        /*
        Probably should bother about this, but there are possible bug
        You can drop item and equip it to some other entity for example: zombie (зондре перец💀🌶️)
        */

        if (!(event.getWhoClicked() instanceof Player player)) return;

        Bukkit.getScheduler().runTask(WeltenRaces.getProvidingPlugin(this.getClass()), () -> refreshModifiers(player));
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        for (ItemStack item : event.getInventory().getContents()) {
            if (item == null) continue;
            ItemManager.tryUndo(item);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        for (ItemStack item : event.getInventory().getContents()) {
            if (item == null) continue;
            ItemManager.tryUndo(item);
        }
    }
}