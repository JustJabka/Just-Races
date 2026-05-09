package justjabka.WeltenRaces.Runnables.Ability;

import justjabka.WeltenRaces.Abilities.PoisonousAreaAbility;
import justjabka.WeltenRaces.Managers.AbilityManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class PoisonousAreaAbilityRunnable extends BukkitRunnable {
    private final UUID pid;

    public PoisonousAreaAbilityRunnable(UUID pid) {
        this.pid = pid;
    }

    @Override
    public void run() {
        Player player = Bukkit.getPlayer(pid);

        if (player == null) {
            PoisonousAreaAbility.stopTask(pid);
            return;
        }

        boolean isActive = AbilityManager.isAbilityActive(player, PoisonousAreaAbility.POISONOUS_AREA_ABILITY_KEY);
        boolean canKeepAlive = PoisonousAreaAbility.keepAliveRequirement(player);

        if (!isActive) {
            PoisonousAreaAbility.stopTask(pid);
            return;
        }

        if (!canKeepAlive) {
            AbilityManager.changeAbilityState(player, PoisonousAreaAbility.POISONOUS_AREA_ABILITY_KEY, false);
            PoisonousAreaAbility.stopTask(pid);
            return;
        }

        PoisonousAreaAbility.whileActive(player);
    }
}
