package justjabka.WeltenRaces.Listeners;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.event.entity.EntityEquipmentChangedEvent;
import justjabka.WeltenRaces.Managers.ArmorManager;
import justjabka.WeltenRaces.Managers.EffectManager;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import static justjabka.WeltenRaces.Managers.ModifierManager.refreshModifiers;
import static justjabka.WeltenRaces.Managers.ModifierManager.tryUndoInventory;

public class BaseRaceListener implements Listener {
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

        Bukkit.getScheduler().runTask(WeltenRaces.INSTANCE, () -> refreshModifiers(player));
    }

    @EventHandler
    public void onPickupItem(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        Bukkit.getScheduler().runTask(WeltenRaces.INSTANCE, () -> refreshModifiers(player));
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        tryUndoInventory(event.getInventory().getContents());
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        tryUndoInventory(event.getInventory().getContents());
    }

    //
    @EventHandler(priority = EventPriority.NORMAL)
    public void onConsume(PlayerItemConsumeEvent event) {
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