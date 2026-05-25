package justjabka.JustRaces.Runnables.Ability;

import justjabka.JustRaces.Abilities.Generic.BaseValidationAbility;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class WildHuntAbilityRunnable extends BukkitRunnable {
    private final BaseValidationAbility ability;
    private final UUID attackerId;
    private final UUID victimId;

    public WildHuntAbilityRunnable(BaseValidationAbility ability, UUID playerId, UUID victimId) {
        this.ability = ability;
        this.attackerId = playerId;
        this.victimId = victimId;
    }

    @Override
    public void run() {
        Player attacker = Bukkit.getPlayer(attackerId);
        Player victim = Bukkit.getPlayer(victimId);

        if (victim == null) {
            this.cancel();
            return;
        }

        if (!ability.isStateValid(victim)) {
            ability.onDeactivation(victim);
            this.cancel();
            return;
        }

        if (attacker == null) return;

        onUseEffects(attacker, victim);
    }

    private static void onUseEffects(Player attacker, Player victim) {
        attacker.spawnParticle(
                Particle.END_ROD,
                victim.getX(),
                victim.getBoundingBox().getCenterY(),
                victim.getZ(),
                10,
                0.25,
                0.5,
                0.25,
                0.01
        );
    }
}
