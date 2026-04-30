package justjabka.WeltenRaces.Abilities;

import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.Configs.Abilities.UnfoldWingsConfig;
import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.Types.Race;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

public class UnfoldWings extends BaseAbility {
    UnfoldWingsConfig config;

    public UnfoldWings(UnfoldWingsConfig config) {
        this.config = config;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        super.handleInteract(event);
    }

    @Override
    public long getCooldownTicks() {
        return config.cooldown;
    }

    @Override
    public String getDisplayName() {
        return "Unfold Wings";
    }

    @Override
    protected boolean canActivate(Player player) {
        return RaceManager.getRace(player) == Race.PHANTOM;
    }

    @Override
    protected boolean onActivation(Player player) {
        WeltenRaces.LOGGER.info("Unfold Wings");

        return true;
    }
}
