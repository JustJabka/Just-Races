package justjabka.JustRaces.Listeners;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.event.entity.EntityEquipmentChangedEvent;
import justjabka.JustRaces.JustRacesAPI;
import justjabka.JustRaces.Managers.AbilityManager;
import justjabka.JustRaces.Managers.ArmorManager;
import justjabka.JustRaces.Managers.EffectManager;
import justjabka.JustRaces.Managers.ModifierManager;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
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
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class GlobalListener implements Listener {
    // Inventory
    @EventHandler(ignoreCancelled = true)
    public void onArmorChange(EntityEquipmentChangedEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        ArmorManager.updateArmorSet(player);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryAction(InventoryClickEvent event) {
        // зондре перец💀🌶️ was there

        if (!(event.getWhoClicked() instanceof Player player)) return;

        Bukkit.getScheduler().runTask(JustRacesAPI.getInstance(), () -> ModifierManager.refreshModifiers(player));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPickupItem(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        ItemStack item = event.getItem().getItemStack();

        ModifierManager.refreshModifiersOnItem(player, item);
        event.getItem().setItemStack(item);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onDropItem(EntityDropItemEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        ItemStack item = event.getItemDrop().getItemStack();

        ModifierManager.tryUndo(item);
        event.getItemDrop().setItemStack(item);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent event) {
        ModifierManager.tryUndoInventory(event.getInventory().getContents());
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClose(InventoryCloseEvent event) {
        ModifierManager.tryUndoInventory(event.getInventory().getContents());
    }

    // Interactions
    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        inventoryRefresh(player);
        AbilityManager.clearAbilitiesStates(player);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        inventoryRefresh(player);
        AbilityManager.endAbilities(player);
    }

    private static void inventoryRefresh(Player player) {
        Bukkit.getScheduler().runTask(JustRacesAPI.getInstance(), () -> {
            ArmorManager.updateArmorSet(player);
            ModifierManager.refreshModifiers(player);
        });
    }

    @SuppressWarnings("UnstableApiUsage")
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();

        AttributeInstance maxAbsorptionInstance = player.getAttribute(Attribute.MAX_ABSORPTION);
        if (maxAbsorptionInstance == null) return;
        if (maxAbsorptionInstance.getValue() <= 0) return;

        Consumable consumable = event.getItem().getData(DataComponentTypes.CONSUMABLE);
        if (consumable == null) return;

        double bonus = EffectManager.calcAbsorptionAmountFromConsumable(consumable);
        if (bonus <= 0) return;

        double current = player.getAbsorptionAmount();
        double limit = maxAbsorptionInstance.getValue();

        player.setAbsorptionAmount(Math.min(current + bonus, limit));
    }
}