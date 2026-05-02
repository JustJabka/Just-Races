package justjabka.WeltenRaces.Abilities;

import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.Configs.Abilities.WildHuntConfig;
import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.Runnables.WildHuntAbilityRunnable;
import justjabka.WeltenRaces.Types.Race;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.FluidCollisionMode;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
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

public class WildHunt extends BaseAbility {
    private final WildHuntConfig config;

    public WildHunt(WildHuntConfig config) {
        this.config = config;
    }

    public static final NamespacedKey WILD_HUNT_KEY = new NamespacedKey(WeltenRaces.NAMESPACE, "wild_hunt");
    public static final Set<PotionEffect> VICTIM_EFFECTS = Set.of(
            new PotionEffect(
                    PotionEffectType.DARKNESS,
                    -1,
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

    @Override
    protected boolean canActivate(Player player) {
        return RaceManager.getRace(player) == Race.PHANTOM;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!AbilityManager.isAbilityActive(player, WILD_HUNT_KEY)) return;
        clearAbility(player);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getPlayer();
        Player attacker = victim.getKiller();

        if (!AbilityManager.isAbilityActive(victim, WILD_HUNT_KEY)) return;

        if (attacker == null) return;
        if (RaceManager.getRace(attacker) != Race.PHANTOM) return;

        clearAbility(victim);
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
        
        if (AbilityManager.isAbilityActive(victim, WILD_HUNT_KEY)) return false;
        giveAbility(player, victim);
        
        return true;
    }

    public void giveAbility(Player player, Player victim) {
        AbilityManager.changeAbilityState(victim, WILD_HUNT_KEY, true);
        
        // Play sounds
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PHANTOM_AMBIENT, SoundCategory.PLAYERS, 1, 1);
        victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_PHANTOM_AMBIENT, SoundCategory.PLAYERS, 1, 1);
        
        // Add effects
        VICTIM_EFFECTS.forEach(victim::addPotionEffect);
        new WildHuntAbilityRunnable(config, player.getUniqueId(), victim.getUniqueId()).runTaskTimer(WeltenRaces.INSTANCE, 0, 20L);
    }

    public static void clearAbility(Player victim) {
        AbilityManager.changeAbilityState(victim, WILD_HUNT_KEY, false);
        WildHunt.VICTIM_EFFECTS.forEach(effect -> victim.removePotionEffect(effect.getType()));
    }

    @Override
    protected boolean activateAction(PlayerInteractEvent event, Player player) {
        if (event.getHand() == EquipmentSlot.OFF_HAND) return false;
        if (!event.getAction().isLeftClick()) return false;
        if (!player.isSneaking()) return false;

        return true;
    }
}
