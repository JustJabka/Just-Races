package justjabka.WeltenRaces.Managers;

import justjabka.WeltenRaces.Abilites.BaseAbility;
import justjabka.WeltenRaces.Abilites.TestAbility;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class AbilityManager {
    public void loadAbilityListeners() {
        List<BaseAbility> abilities = List.of(
                new TestAbility()
        );

        for (BaseAbility ability : abilities) {
            Bukkit.getPluginManager().registerEvents(ability, WeltenRaces.INSTANCE);
        }

        WeltenRaces.LOGGER.info("Successfully loaded abilities!");
    }

    public static boolean hasActivationSlotSelected(Player player) {
        int hotbarSlot = player.getInventory().getHeldItemSlot();
        return hotbarSlot == 8;
    }
}
