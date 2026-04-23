package justjabka.WeltenRaces.Abilites;

import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.Types.Race;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

public class TestAbility extends BaseAbility {
    @Override
    public long getCooldownTicks() {
        return 60;
    }

    @Override
    public String getDisplayName() {
        return "TestAbility";
    }

    @Override
    protected boolean canActivate(Player player) {
        return RaceManager.getRace(player) == Race.ARMAT;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        super.handleInteract(event);
    }

    @Override
    protected boolean onActivation(Player player) {
        player.sendMessage(Component.text("activated!"));
        return true;
    }
}