package justjabka.WeltenRaces.Abilites;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class TestAbility extends BaseAbility implements Listener {
    @Override
    public long getCooldownTicks() {
        return 60;
    }

    @Override
    public String getDisplayName() {
        return "TestAbility";
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