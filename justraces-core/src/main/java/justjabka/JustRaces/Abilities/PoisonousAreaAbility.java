package justjabka.JustRaces.Abilities;

import justjabka.JustRaces.Abilities.Generic.BaseTogglableAbility;
import justjabka.JustRaces.Configs.Ability.PoisonousAreaAbilityConfig;
import justjabka.JustRaces.JustRacesAPI;
import justjabka.JustRaces.Managers.AbilityManager;
import justjabka.JustRaces.Runnables.Ability.PoisonousAreaAbilityRunnable;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PoisonousAreaAbility extends BaseTogglableAbility {
    private final PoisonousAreaAbilityConfig config;
    private final Map<UUID, BukkitTask> activeTasks = new HashMap<>();
    private static final Material activationItem = Material.SPORE_BLOSSOM;

    public PoisonousAreaAbility(PoisonousAreaAbilityConfig config) {
        this.config = config;
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesAPI.NAMESPACE, "poisonous_area");
    }

    @Override
    public long getCooldownTicks() {
        return config.cooldown;
    }

    @EventHandler
    public void handleInteract(PlayerInteractEvent event) {
        super.handleInteract(event);
    }

    @Override
    protected boolean canActivate(Player player) {
        return isStateValid(player);
    }

    @Override
    protected boolean onActivation(Player player, Object... ctx) {
        toggle(player);
        return true;
    }

    @Override
    public void onDeactivation(Player player) {
        disable(player);
    }

    @Override
    public boolean isStateValid(Player player) {
        ItemStack mainHand = player.getInventory().getItemInMainHand();

        if (mainHand.isEmpty()) return false;
        if (mainHand.getType() != activationItem) return false;

        return mainHand.getAmount() >= config.fuelDrainAmount;
    }

    @Override
    public void enable(Player player) {
        AbilityManager.setAbilityState(player, getKey(), true);

        UUID pid = player.getUniqueId();

        BukkitTask task = new PoisonousAreaAbilityRunnable(this, config, pid).runTaskTimer(JustRacesAPI.getInstance(), 0, config.effectDuration);
        activeTasks.put(pid, task);
    }

    @Override
    public void stopTask(UUID pid) {
        BukkitTask task = activeTasks.remove(pid);
        if (task != null) task.cancel();
    }
}
