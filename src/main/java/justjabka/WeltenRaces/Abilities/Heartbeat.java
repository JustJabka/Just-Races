package justjabka.WeltenRaces.Abilities;

import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.Types.Race;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class Heartbeat extends BaseAbility {
    @Override
    public long getCooldownTicks() {
        return 5 * 20;
    }

    @Override
    public String getDisplayName() {
        return "Heartbeat";
    }

    @Override
    protected boolean canActivate(Player player) {
        if (RaceManager.getRace(player) != Race.PHANTOM) return false;
        return true;
    }

    @Override
    protected boolean onActivation(Player player) {
        for (LivingEntity victim : player.getLocation().getNearbyLivingEntities(10)) {
            if (victim == player) continue;
            WeltenRaces.LOGGER.info("set glowing");
        }

        return true;
    }

    @Override
    protected boolean activateAction(PlayerInteractEvent event, Player player) {
        if (event.getHand() == EquipmentSlot.OFF_HAND) return false;
        if (!event.getAction().isRightClick()) return false;
        if (!player.isSneaking()) return false;

        return true;
    }
}
