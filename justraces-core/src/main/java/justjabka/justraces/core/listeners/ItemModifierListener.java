package justjabka.justraces.core.listeners;

import io.papermc.paper.event.entity.EntityEquipmentChangedEvent;
import justjabka.justraces.api.JustRacesAPI;
import justjabka.justraces.api.managers.ItemModifierManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class ItemModifierListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onArmorChange(EntityEquipmentChangedEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        ItemModifierManager.refreshModifiers(player);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryAction(InventoryClickEvent event) {
        // зондре перец💀🌶️ was there

        if (!(event.getWhoClicked() instanceof Player player)) return;

        inventoryRefresh(player);
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();

        ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
        ItemModifierManager.refreshModifiersOnItem(player, newItem);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPickupItem(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        ItemStack item = event.getItem().getItemStack();

        ItemModifierManager.refreshModifiersOnItem(player, item);
        event.getItem().setItemStack(item);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onDropItem(EntityDropItemEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        ItemStack item = event.getItemDrop().getItemStack();

        ItemModifierManager.tryUndo(item);
        event.getItemDrop().setItemStack(item);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent event) {
        ItemModifierManager.tryUndoInventory(event.getInventory().getContents());
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClose(InventoryCloseEvent event) {
        ItemModifierManager.tryUndoInventory(event.getInventory().getContents());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        inventoryRefresh(player);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();

        inventoryRefresh(player);
    }

    private static void inventoryRefresh(Player player) {
        Bukkit.getScheduler().runTask(JustRacesAPI.getInstance(), () ->
                ItemModifierManager.refreshModifiers(player)
        );
    }
}
