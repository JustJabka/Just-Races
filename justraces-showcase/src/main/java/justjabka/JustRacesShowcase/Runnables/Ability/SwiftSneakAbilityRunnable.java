package justjabka.JustRacesShowcase.Runnables.Ability;

import justjabka.JustRaces.Abilities.Generic.BaseValidationAbility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class SwiftSneakAbilityRunnable extends BukkitRunnable {
    private final BaseValidationAbility ability;
    private final UUID pid;

    public SwiftSneakAbilityRunnable(BaseValidationAbility ability, UUID pid) {
        this.ability = ability;
        this.pid = pid;
    }

    @Override
    public void run() {
        Player player = Bukkit.getPlayer(pid);

        if (player == null) {
            this.cancel();
            return;
        }

        if (!ability.isStateValid(player)) {
            ability.onDeactivation(player);

            this.cancel();
        }
    }
}
