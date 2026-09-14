package justjabka.justraces.core.listeners;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import justjabka.justraces.api.managers.AbilityManager;
import justjabka.justraces.api.common.entry.AbilityEntry;
import justjabka.justraces.api.abilities.AbilityTrigger;
import justjabka.justraces.api.abilities.AbilityTriggerCondition;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.Set;

public class TriggerListener implements Listener {
    private static final int CHESTPLATE_SLOT = 38;

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        AbilityTrigger trigger;
        if (action.isLeftClick()) trigger = AbilityTrigger.LEFT_CLICK;
        else if (action.isRightClick()) trigger = AbilityTrigger.RIGHT_CLICK;
        else return;

        triggerAndCancel(player, trigger, event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        boolean isSneaking = event.isSneaking();


        AbilityTrigger trigger = isSneaking ? AbilityTrigger.SNEAK_ON : AbilityTrigger.SNEAK_OFF;
        if (triggerAndCancel(player, trigger, event)) {
            return;
        }

        triggerAndCancel(player, AbilityTrigger.SNEAK_TOGGLE, event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJump(PlayerJumpEvent event) {
        Player player = event.getPlayer();
        triggerAndCancel(player, AbilityTrigger.JUMP, event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        triggerAndCancel(player, AbilityTrigger.OFFHAND_SWAP, event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerRightClickChestplate(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        if (event.getClick() != ClickType.RIGHT) return;

        if (event.getSlotType() != InventoryType.SlotType.ARMOR) return;
        if (event.getSlot() != CHESTPLATE_SLOT) return;

        triggerAndCancel(player, AbilityTrigger.RIGHT_CLICK_CHESTPLATE, event);
    }

    private static boolean triggerAndCancel(Player player, AbilityTrigger requiredTrigger, Event event) {
        boolean triggered = triggerAbilities(player, requiredTrigger);
        if (!triggered) return false;

        if (!(event instanceof Cancellable cancellable)) return false;
        cancellable.setCancelled(true);

        return true;
    }

    private static boolean triggerAbilities(Player player, AbilityTrigger requiredTrigger) {
        Set<AbilityEntry> raceAbilities = AbilityManager.getAbilityEntriesForPlayer(player);

        for (AbilityEntry ability : raceAbilities) {
            if (ability.trigger() != requiredTrigger) continue;

            if (!isConditionsMet(player, ability)) continue;

            if (ability.ability().tryActivate(player)) return true;
        }

        return false;
    }

    private static boolean isConditionsMet(Player player, AbilityEntry ability) {
        Set<AbilityTriggerCondition> conditions = ability.conditions();

        if (conditions == null || conditions.isEmpty()) {
            return true;
        }

        for (AbilityTriggerCondition condition : conditions) {
            if (condition.test(player)) continue;
            return false;
        }

        return true;
    }
}
