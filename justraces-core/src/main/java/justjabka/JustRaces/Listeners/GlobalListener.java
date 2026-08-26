package justjabka.JustRaces.Listeners;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.event.entity.EntityEquipmentChangedEvent;
import justjabka.JustRaces.JustRacesAPI;
import justjabka.JustRaces.Managers.AbilityManager;
import justjabka.JustRaces.Managers.ArmorManager;
import justjabka.JustRaces.Managers.EffectManager;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import static justjabka.JustRaces.Managers.ModifierManager.refreshModifiers;
import static justjabka.JustRaces.Managers.ModifierManager.tryUndoInventory;

public class GlobalListener implements Listener {
    // Inventory
    @EventHandler
    public void onArmorChange(EntityEquipmentChangedEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        ArmorManager.updateArmorSet(player);
    }

    @EventHandler
    public void onInventoryAction(InventoryClickEvent event) {
        /*
        Probably shouldn't bother about this, but there are possible bug
        You can drop item and equip it to some other entity for example: zombie (зондре перец💀🌶️)
        */

        if (!(event.getWhoClicked() instanceof Player player)) return;

        Bukkit.getScheduler().runTask(JustRacesAPI.getInstance(), () -> refreshModifiers(player));
    }

    @EventHandler
    public void onPickupItem(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        Bukkit.getScheduler().runTask(JustRacesAPI.getInstance(), () -> refreshModifiers(player));
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        tryUndoInventory(event.getInventory().getContents());
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        tryUndoInventory(event.getInventory().getContents());
    }

    // Interactions
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        inventoryRefresh(player);
        AbilityManager.clearAbilitiesStates(player);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        inventoryRefresh(player);
        AbilityManager.endAbilities(player);
    }

    private static void inventoryRefresh(Player player) {
        Bukkit.getScheduler().runTask(JustRacesAPI.getInstance(), () -> {
            ArmorManager.updateArmorSet(player);
            refreshModifiers(player);
        });
    }

    @SuppressWarnings("UnstableApiUsage")
    @EventHandler(priority = EventPriority.NORMAL)
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