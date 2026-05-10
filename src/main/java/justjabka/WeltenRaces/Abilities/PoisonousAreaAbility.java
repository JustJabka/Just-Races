package justjabka.WeltenRaces.Abilities;

import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.Configs.Ability.PoisonousAreaAbilityConfig;
import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.Runnables.Ability.PoisonousAreaAbilityRunnable;
import justjabka.WeltenRaces.WeltenRaces;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
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

public class PoisonousAreaAbility extends BaseAbility {
    private final PoisonousAreaAbilityConfig config;
    private final Map<UUID, BukkitTask> activeTasks = new HashMap<>();
    private static final Material activationItem = Material.SPORE_BLOSSOM;

    public PoisonousAreaAbility(PoisonousAreaAbilityConfig config) {
        this.config = config;
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(WeltenRaces.NAMESPACE, "poisonous_area");
    }

    @Override
    public long getCooldownTicks() {
        return config.cooldown;
    }

    @Override
    public Component getAbilityDisplay(Player player) {
        boolean isActive = AbilityManager.isAbilityActive(player, getKey());

        Component displayName = getDisplayName();
        TextColor displayColor = isActive ? ABILITY_READY_COLOR : ABILITY_ON_COOLDOWN_COLOR;

        return Component
                .translatable("ability.poisonous_area.state")
                .fallback("%s")
                .arguments(displayName)
                .color(displayColor)
                .decorate(TextDecoration.UNDERLINED);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        super.handleInteract(event);
    }

    @Override
    protected boolean canActivate(Player player) {
        return isStateValid(player);
    }

    @Override
    protected boolean onActivation(Player player) {
        toggleAbility(player);
        return true;
    }

    @Override
    public boolean isStateValid(Player player) {
        ItemStack mainHand = player.getInventory().getItemInMainHand();

        if (mainHand.isEmpty()) return false;
        if (mainHand.getType() != activationItem) return false;

        return mainHand.getAmount() >= config.fuelDrainAmount;
    }

    @Override
    public void onDeactivation(Player player) {
        AbilityManager.changeAbilityState(player, getKey(), false);
        stopTask(player.getUniqueId());
    }

    public void toggleAbility(Player player) {
        UUID pid = player.getUniqueId();

        boolean currentState = AbilityManager.isAbilityActive(player, getKey());
        boolean newState = !currentState;

        AbilityManager.changeAbilityState(player, getKey(), newState);

        if (newState) {
            BukkitTask task = new PoisonousAreaAbilityRunnable(this, config, pid).runTaskTimer(WeltenRaces.INSTANCE, 0, config.effectDuration);
            activeTasks.put(pid, task);
        } else {
            stopTask(pid);
        }
    }

    @Override
    public void stopTask(UUID pid) {
        BukkitTask task = activeTasks.remove(pid);
        if (task != null) task.cancel();
    }
}
