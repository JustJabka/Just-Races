package justjabka.JustRaces.Runnables.Ability;

import justjabka.JustRaces.Abilities.Generic.BaseTogglableAbility;
import justjabka.JustRaces.Configs.Ability.PoisonousAreaAbilityConfig;
import justjabka.JustRaces.Managers.AbilityManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class PoisonousAreaAbilityRunnable extends BukkitRunnable {
    private final BaseTogglableAbility ability;
    private final PoisonousAreaAbilityConfig config;
    private final UUID pid;
    private final PotionEffect cloudEffect;

    public PoisonousAreaAbilityRunnable(BaseTogglableAbility ability, PoisonousAreaAbilityConfig config, UUID pid) {
        this.ability = ability;
        this.config = config;
        this.pid = pid;
        this.cloudEffect = new PotionEffect(
                PotionEffectType.POISON,
                config.effectDuration,
                0,
                false,
                true,
                true
        );
    }

    @Override
    public void run() {
        Player player = Bukkit.getPlayer(pid);

        if (player == null) {
            ability.stopTask(pid);
            this.cancel();
            return;
        }

        boolean isActive = AbilityManager.isAbilityActive(player, ability.getKey());

        if (!isActive || !ability.isStateValid(player)) {
            ability.onDeactivation(player);
            this.cancel();
            return;
        }

        whileActive(player);
    }

    private void whileActive(Player player) {
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        mainHand.subtract(config.fuelDrainAmount);

        player.getWorld().spawn(
                player.getLocation(),
                AreaEffectCloud.class,
                CreatureSpawnEvent.SpawnReason.CUSTOM,
                cloud -> {
                    cloud.setRadius(config.effectRadius);
                    cloud.setDuration(config.effectDuration);
                    cloud.setSource(player);
                    cloud.addCustomEffect(cloudEffect, true);
                }
        );
    }
}
