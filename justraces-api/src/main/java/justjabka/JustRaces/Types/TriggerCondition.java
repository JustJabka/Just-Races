package justjabka.JustRaces.Types;

import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

import java.util.function.Predicate;

public enum TriggerCondition {
    EMPTY_HAND(player -> isEmptySlot(player, EquipmentSlot.HAND)),
    EMPTY_OFFHAND(player -> isEmptySlot(player, EquipmentSlot.OFF_HAND)),
    SNEAKING(Player::isSneaking),
    ON_GROUND(Player::isOnGround),
    GLIDING(Player::isGliding);

    private final Predicate<Player> predicate;

    TriggerCondition(Predicate<Player> predicate) {
        this.predicate = predicate;
    }

    public boolean test(Player player) {
        return predicate.test(player);
    }

    private static boolean isEmptySlot(Player player, EquipmentSlot slot) {
        return player.getInventory().getItem(slot).isEmpty();
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
