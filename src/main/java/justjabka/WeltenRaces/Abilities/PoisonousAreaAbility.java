package justjabka.WeltenRaces.Abilities;

import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.Runnables.Ability.PoisonousAreaAbilityRunnable;
import justjabka.WeltenRaces.WeltenRaces;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PoisonousAreaAbility extends BaseAbility {
    public static final NamespacedKey POISONOUS_AREA_ABILITY_KEY = new NamespacedKey(WeltenRaces.NAMESPACE, "poisonous_area");
    private static final Map<UUID, BukkitTask> ACTIVE_TASKS = new HashMap<>();

    private static final Material ACTIVATION_ITEM = Material.SPORE_BLOSSOM;
    private static final int DRAIN_AMOUNT = 1;

    private static final int EFFECT_DURATION = 3 * 20;
    private static final int CLOUD_RADIUS = 10;
    private static final PotionEffect CLOUD_EFFECT = new PotionEffect(
            PotionEffectType.POISON,
            EFFECT_DURATION,
            0,
            false,
            true,
            true
    );

    @Override
    public long getCooldownTicks() {
        return 60;
    }

    @Override
    public Component getAbilityDisplay(Player player) {
        boolean isActive = AbilityManager.isAbilityActive(player, POISONOUS_AREA_ABILITY_KEY);

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
        return keepAliveRequirement(player);
    }

    public static boolean keepAliveRequirement(Player player) {
        ItemStack mainHand = player.getInventory().getItemInMainHand();

        if (mainHand.isEmpty()) return false;
        if (mainHand.getType() != ACTIVATION_ITEM) return false;

        return mainHand.getAmount() >= DRAIN_AMOUNT;
    }

    @Override
    protected boolean onActivation(Player player) {
        toggleAbility(player);
        return true;
    }

    public static void toggleAbility(Player player) {
        UUID pid = player.getUniqueId();

        boolean currentState = AbilityManager.isAbilityActive(player, POISONOUS_AREA_ABILITY_KEY);
        boolean newState = !currentState;

        AbilityManager.changeAbilityState(player, POISONOUS_AREA_ABILITY_KEY, newState);

        if (newState) {
            BukkitTask task = new PoisonousAreaAbilityRunnable(pid).runTaskTimer(WeltenRaces.INSTANCE, 0, EFFECT_DURATION);
            ACTIVE_TASKS.put(pid, task);
        } else {
            stopTask(pid);
        }
    }

    public static void stopTask(UUID pid) {
        BukkitTask task = ACTIVE_TASKS.remove(pid);

        if (task == null) return;
        task.cancel();
    }

    public static void whileActive(Player player) {
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        mainHand.subtract(DRAIN_AMOUNT);

        player.getWorld().spawn(
                player.getLocation(),
                AreaEffectCloud.class,
                CreatureSpawnEvent.SpawnReason.CUSTOM,
                cloud -> {
                    cloud.setRadius(CLOUD_RADIUS);
                    cloud.setDuration(EFFECT_DURATION);
                    cloud.setSource(player);
                    cloud.addCustomEffect(CLOUD_EFFECT, true);
                }
        );
    }
}
