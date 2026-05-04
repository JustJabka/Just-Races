package justjabka.WeltenRaces.Abilities;

import com.jeff_media.morepersistentdatatypes.DataType;
import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.Configs.Abilities.WildHuntConfig;
import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.Runnables.Ability.WildHuntAbilityRunnable;
import justjabka.WeltenRaces.Types.Race;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;

import java.util.Set;
import java.util.UUID;

public class WildHuntAbility extends BaseAbility {
    private final WildHuntConfig config;

    public WildHuntAbility(WildHuntConfig config) {
        this.config = config;
    }

    public static final NamespacedKey WILD_HUNT_KEY = new NamespacedKey(WeltenRaces.NAMESPACE, "wild_hunt");
    public static final Set<PotionEffect> VICTIM_EFFECTS = Set.of(
            new PotionEffect(
                    PotionEffectType.DARKNESS,
                    PotionEffect.INFINITE_DURATION,
                    0,
                    false,
                    true,
                    true
            )
    );

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        super.handleInteract(event);
    }

    @Override
    public long getCooldownTicks() {
        return config.cooldown;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!AbilityManager.hasAbility(player, WILD_HUNT_KEY)) return;
        clearAbility(player);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getPlayer();
        Player attacker = victim.getKiller();

        if (!AbilityManager.hasAbility(victim, WILD_HUNT_KEY)) return;
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
        
        if (AbilityManager.hasAbility(victim, WILD_HUNT_KEY)) return false;
        giveAbility(player, victim);
        
        return true;
    }

    public void giveAbility(Player attacker, Player victim) {
        UUID playerId = attacker.getUniqueId();
        UUID victimId = victim.getUniqueId();

        clearPreviousVictim(victimId, playerId);
        AbilityManager.changeAbilityOwner(victim, WILD_HUNT_KEY, playerId);
        
        // Play sounds
        attacker.getWorld().playSound(attacker.getLocation(), Sound.ENTITY_PHANTOM_AMBIENT, SoundCategory.PLAYERS, 1, 1);
        victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_PHANTOM_AMBIENT, SoundCategory.PLAYERS, 1, 1);
        
        // Add effects
        VICTIM_EFFECTS.forEach(victim::addPotionEffect);
        new WildHuntAbilityRunnable(config, playerId, victimId).runTaskTimer(WeltenRaces.INSTANCE, 0, 20L);
    }

    private static void clearPreviousVictim(UUID victimId, UUID playerId) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.getUniqueId().equals(victimId)) continue;

            if (!AbilityManager.hasAbility(online, WILD_HUNT_KEY)) continue;

            UUID currentOwner = getCurrentOwner(online);
            if (!playerId.equals(currentOwner)) continue;

            clearAbility(online);
        }
    }

    public static UUID getCurrentOwner(Player player) {
        return AbilityManager.getAbilities(player).get(WILD_HUNT_KEY, DataType.UUID);
    }

    public static void clearAbility(Player victim) {
        AbilityManager.removeAbility(victim, WILD_HUNT_KEY);
        WildHuntAbility.VICTIM_EFFECTS.forEach(effect -> victim.removePotionEffect(effect.getType()));
    }

    @Override
    protected boolean activateAction(PlayerInteractEvent event, Player player) {
        if (event.getHand() == EquipmentSlot.OFF_HAND) return false;
        if (!event.getAction().isLeftClick()) return false;
        if (!player.isSneaking()) return false;

        return true;
    }
}
