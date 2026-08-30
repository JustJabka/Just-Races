package justjabka.JustRacesShowcase.Abilities;

import justjabka.JustRaces.Abilities.Generic.BaseAbility;
import justjabka.JustRaces.Abilities.Generic.DurationAbility;
import justjabka.JustRaces.Abilities.Generic.RunnableAbility;
import justjabka.JustRacesShowcase.JustRacesShowcase;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SlimeTrailAbility extends BaseAbility implements DurationAbility, RunnableAbility {
    private final Set<PotionEffect> trailEffects = Set.of(
            new PotionEffect(PotionEffectType.SLOWNESS, getConfigInt("trail", "effects_duration"), 1, false, true, true),
            new PotionEffect(PotionEffectType.OOZING, getConfigInt("trail", "effects_duration"), 0, false, true, true)
    );
    private final Map<UUID, BukkitTask> activeTrails = new ConcurrentHashMap<>();

    private static final Color TRAIL_COLOR = Color.fromRGB(153, 255, 163);

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "slime_trail");
    }

    @Override
    public long getCooldownTicks() {
        return getConfigCooldown();
    }

    @Override
    public long getDurationTicks() {
        return getConfigDuration();
    }

    @Override
    public BossBar.Color getCooldownBarColor(Player player) {
        return BossBar.Color.GREEN;
    }

    @EventHandler(ignoreCancelled = true)
    public void onTrigger(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!event.getAction().isLeftClick()) return;

        if (!player.isSneaking()) return;
        if (!player.getInventory().getItemInMainHand().isEmpty()) return;

        if (!tryActivate(player)) return;
        event.setCancelled(true);
    }

    @Override
    protected boolean canActivate(Player player) {
        return player.isOnGround();
    }

    @Override
    protected boolean onActivation(Player player, Object... ctx) {
        BukkitTask task = createRunnable(player).runTaskTimer(JustRacesShowcase.INSTANCE, 0L, 4L);
        activeTrails.put(player.getUniqueId(), task);
        return true;
    }

    @EventHandler(ignoreCancelled = true)
    public void onCloudApply(AreaEffectCloudApplyEvent event) {
        if (!(event.getEntity().getSource() instanceof Player sourcePlayer)) return;
        if (!playerHasAbility(sourcePlayer)) return;

        event.getAffectedEntities().remove(sourcePlayer);
    }

    @Override
    public BukkitRunnable createRunnable(Player player) {
        return new BukkitRunnable() {
            long durationLeft = getDurationTicks();
            final float trailRadius = getConfigFloat("trail", "radius");
            final int trailDuration = getConfigInt("trail", "duration");

            @Override
            public void run() {
                if (durationLeft <= 0) {
                    onExpire(player);
                    return;
                }
                durationLeft -= 4L;

                World world = player.getWorld();
                Location location = player.getLocation();

                if (!player.isOnGround()) return;

                world.spawn(
                        location,
                        AreaEffectCloud.class,
                        CreatureSpawnEvent.SpawnReason.CUSTOM,
                        cloud -> {
                            cloud.setRadius(trailRadius);
                            cloud.setDuration(trailDuration);
                            cloud.setWaitTime(0);
                            cloud.setColor(TRAIL_COLOR);
                            cloud.setSource(player);
                            trailEffects.forEach(effect -> cloud.addCustomEffect(effect, true));
                        }
                );
            }
        };
    }

    @Override
    public void resetState(UUID pid, Reason reason) {
        BukkitTask task = activeTrails.remove(pid);
        if (task == null) return;

        task.cancel();
    }
}
