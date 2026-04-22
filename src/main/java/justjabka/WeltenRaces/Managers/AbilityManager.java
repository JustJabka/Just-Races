package justjabka.WeltenRaces.Managers;

import org.bukkit.entity.Player;

public class AbilityManager {
    public static boolean hasActivationSlotSelected(Player player) {
        int hotbarSlot = player.getInventory().getHeldItemSlot();
        return hotbarSlot == 8;
    }
}
