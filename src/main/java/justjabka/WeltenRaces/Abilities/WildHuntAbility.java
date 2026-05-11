package justjabka.WeltenRaces.Abilities;

import com.jeff_media.morepersistentdatatypes.DataType;
import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.Configs.Ability.WildHuntAbilityConfig;
import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.Runnables.Ability.WildHuntAbilityRunnable;
import justjabka.WeltenRaces.Types.AbilityActivateAction;
import justjabka.WeltenRaces.Types.Race;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;

import java.util.Set;
import java.util.UUID;

public class WildHuntAbility extends BaseAbility {
    private final WildHuntAbilityConfig config;
    private final double radiusSquared;
    private static final Set<PotionEffect> victimEffects = Set.of(
            new PotionEffect(
                    PotionEffectType.DARKNESS,
                    PotionEffect.INFINITE_DURATION,
                    0,
                    false,
                    true,
                    true
            )
    );

    public WildHuntAbility(WildHuntAbilityConfig config) {
        this.config = config;
        this.radiusSquared = config.radius * config.radius;
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(WeltenRaces.NAMESPACE, "wild_hunt");
    }

    @EventHandler
    public void handleInteract(PlayerInteractEvent event) {
        super.handleInteract(event);
    }

    @Override
    public long getCooldownTicks() {
        return config.cooldown;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!AbilityManager.hasAbility(player, getKey())) return;
        clearAbility(player);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getPlayer();
        Player attacker = victim.getKiller();

        if (!AbilityManager.hasAbility(victim, getKey())) return;
        clearAbility(victim);

        if (attacker == null) return;
        if (RaceManager.getRace(attacker) != Race.PHANTOM) return;
        setCooldownTicks(attacker, 0L);
    }

    @Override
    protected boolean onActivation(Player player) {
        RayTraceResult raycast = player.getWorld().rayTrace(
                player.getEyeLocation(),
                player.getEyeLocation().getDirection(),
                config.radius,
                FluidCollisionMode.NEVER,
                true,
                0.5,
                (entity) -> entity instanceof Player && !entity.equals(player)
        );
        if (raycast == null) return false;

        Entity target = raycast.getHitEntity();
        if (target == null) return false;
        if (!(target instanceof  Player victim)) return false;
        
        if (AbilityManager.hasAbility(victim, getKey())) return false;
        giveAbility(player, victim);
        
        return true;
    }

    @Override
    public void onDeactivation(Player player) {
        clearAbility(player);
    }

    @Override
    public boolean isStateValid(Player victim) {
        UUID attackerId = getCurrentOwner(victim);
        if (attackerId == null) return false;

        Player attacker = Bukkit.getPlayer(attackerId);
        if (attacker == null) return false;

        double distanceSquared = attacker.getLocation().distanceSquared(victim.getLocation());
        return distanceSquared <= radiusSquared;
    }

    @Override
    protected boolean interactionAction(PlayerInteractEvent event, Player player) {
        return AbilityActivateAction.SHIFT_LEFT_CLICK.check(event, player);
    }

    public void giveAbility(Player attacker, Player victim) {
        UUID playerId = attacker.getUniqueId();
        UUID victimId = victim.getUniqueId();

        clearPreviousVictim(victimId, playerId);
        AbilityManager.changeAbilityOwner(victim, getKey(), playerId);
        
        // Play sounds
        attacker.getWorld().playSound(attacker.getLocation(), Sound.ENTITY_PHANTOM_AMBIENT, SoundCategory.PLAYERS, 1, 1);
        victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_PHANTOM_AMBIENT, SoundCategory.PLAYERS, 1, 1);
        
        // Add effects
        victimEffects.forEach(victim::addPotionEffect);
        new WildHuntAbilityRunnable(this, playerId, victimId).runTaskTimer(WeltenRaces.INSTANCE, 0, 20L);
    }

    public void clearAbility(Player victim) {
        AbilityManager.removeAbility(victim, getKey());
        victimEffects.forEach(effect -> victim.removePotionEffect(effect.getType()));
    }

    private void clearPreviousVictim(UUID victimId, UUID playerId) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.getUniqueId().equals(victimId)) continue;

            if (!AbilityManager.hasAbility(online, getKey())) continue;

            UUID currentOwner = getCurrentOwner(online);
            if (!playerId.equals(currentOwner)) continue;

            clearAbility(online);
        }
    }

    private UUID getCurrentOwner(Player player) {
        return AbilityManager.getAbilities(player).get(getKey(), DataType.UUID);
    }
}
