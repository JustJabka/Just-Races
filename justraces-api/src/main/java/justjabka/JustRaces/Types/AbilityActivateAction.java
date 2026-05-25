package justjabka.JustRaces.Types;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.function.BiPredicate;

public enum AbilityActivateAction {
    RIGHT_CLICK((event, player) -> !isOffHandClick(event) && isRightClick(event) && !isSneaking(player)),
    SHIFT_RIGHT_CLICK((event, player) -> !isOffHandClick(event) && isRightClick(event) && isSneaking(player)),
    LEFT_CLICK((event, player) -> !isOffHandClick(event) &&  isLeftClick(event) && !isSneaking(player)),
    SHIFT_LEFT_CLICK((event, player) -> !isOffHandClick(event) && isLeftClick(event) && isSneaking(player));

    private final BiPredicate<PlayerInteractEvent, Player> checker;

    AbilityActivateAction(BiPredicate<PlayerInteractEvent, Player> checker) {
        this.checker = checker;
    }

    public boolean check(PlayerInteractEvent event, Player player) {
        return checker.test(event, player);
    }

    // Checks
    private static boolean isRightClick(PlayerInteractEvent event) {
        return event.getAction().isRightClick();
    }

    private static boolean isLeftClick(PlayerInteractEvent event) {
        return event.getAction().isLeftClick();
    }

    private static boolean isSneaking(Player player) {
        return player.isSneaking();
    }

    private static boolean isOffHandClick(PlayerInteractEvent event) {
        return event.getHand() == EquipmentSlot.OFF_HAND;
    }
}
