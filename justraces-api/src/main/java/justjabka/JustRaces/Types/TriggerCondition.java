package justjabka.JustRaces.Types;

import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

import java.util.function.Predicate;

/**
 * The trigger conditions are used to ensure certain conditions are met when triggering an ability, such as
 * ensuring the player has not clicked a block.
 * <p>
 * Don't confuse ability trigger and ability activation requirements ({@code BaseAbility#canActivate(Player)}). Those are two different things
 * First one is to know that player actually wanted to activate ability.
 * Second one is to know if ability can actually work (player has some armor set equipped or have enough score)
 * @see AbilityBinding
 * @see Trigger
 */
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
