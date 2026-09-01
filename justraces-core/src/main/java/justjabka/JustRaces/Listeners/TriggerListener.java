package justjabka.JustRaces.Listeners;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import justjabka.JustRaces.Managers.AbilityManager;
import justjabka.JustRaces.Types.AbilityBinding;
import justjabka.JustRaces.Types.Trigger;
import justjabka.JustRaces.Types.TriggerCondition;
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

        Trigger trigger;
        if (action.isLeftClick()) trigger = Trigger.LEFT_CLICK;
        else if (action.isRightClick()) trigger = Trigger.RIGHT_CLICK;
        else return;

        triggerAndCancel(player, trigger, event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        boolean isSneaking = event.isSneaking();


        Trigger trigger = isSneaking ? Trigger.SNEAK_ON : Trigger.SNEAK_OFF;
        if (triggerAndCancel(player, trigger, event)) {
            return;
        }

        triggerAndCancel(player, Trigger.SNEAK_TOGGLE, event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJump(PlayerJumpEvent event) {
        Player player = event.getPlayer();
        triggerAndCancel(player, Trigger.JUMP, event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        triggerAndCancel(player, Trigger.OFFHAND_SWAP, event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerRightClickChestplate(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        if (event.getClick() != ClickType.RIGHT) return;

        if (event.getSlotType() != InventoryType.SlotType.ARMOR) return;
        if (event.getSlot() != CHESTPLATE_SLOT) return;

        triggerAndCancel(player, Trigger.RIGHT_CLICK_CHESTPLATE, event);
    }

    private static boolean triggerAndCancel(Player player, Trigger requiredTrigger, Event event) {
        boolean triggered = triggerAbilities(player, requiredTrigger);
        if (!triggered) return false;

        if (!(event instanceof Cancellable cancellable)) return false;
        cancellable.setCancelled(true);

        return true;
    }

    private static boolean triggerAbilities(Player player, Trigger requiredTrigger) {
        Set<AbilityBinding> abilityBindings = AbilityManager.getAbilitiesBindingsForPlayer(player);

        for (AbilityBinding binding : abilityBindings) {
            if (binding.trigger() != requiredTrigger) continue;

            if (!isConditionsMet(player, binding)) continue;

            if (binding.ability().tryActivate(player)) return true;
        }

        return false;
    }

    private static boolean isConditionsMet(Player player, AbilityBinding binding) {
        Set<TriggerCondition> conditions = binding.conditions();

        if (conditions == null || conditions.isEmpty()) {
            return true;
        }

        for (TriggerCondition condition : conditions) {
            if (condition.test(player)) continue;
            return false;
        }

        return true;
    }
}
