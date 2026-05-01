package justjabka.WeltenRaces.Abilities;

import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.Runnables.WildHuntAbilityRunnable;
import justjabka.WeltenRaces.Types.Race;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;

import java.util.Set;

public class WildHunt extends BaseAbility {
    // TODO: add config

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
        return 40;
    }

    @Override
    public String getDisplayName() {
        return "Wild Hunt";
    }

    @Override
    protected boolean canActivate(Player player) {
        return RaceManager.getRace(player) == Race.PHANTOM;
    }

    // TODO: add join and death listeners

    @Override
    protected boolean onActivation(Player player) {
        RayTraceResult raycast = player.getWorld().rayTrace(
                player.getEyeLocation(),
                player.getEyeLocation().getDirection(),
                120,
                FluidCollisionMode.NEVER,
                true,
                0.5,
                (entity) -> entity instanceof LivingEntity && !entity.equals(player)
        );
        if (raycast == null) return false;

        LivingEntity victim = (LivingEntity) raycast.getHitEntity();
        if (victim == null) return false;
        // TODO: add check for already marked entity

        onVictimFound(player, victim);
        return true;
    }

    private static void onVictimFound(Player player, LivingEntity victim) {
        VICTIM_EFFECTS.forEach(victim::addPotionEffect);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PHANTOM_AMBIENT, SoundCategory.PLAYERS, 1, 1);
        victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_PHANTOM_AMBIENT, SoundCategory.PLAYERS, 1, 1);

        new WildHuntAbilityRunnable(player.getUniqueId(), victim.getUniqueId()).runTaskTimer(WeltenRaces.INSTANCE, 0, 20L);
    }

    @Override
    protected boolean activateAction(PlayerInteractEvent event, Player player) {
        if (event.getHand() == EquipmentSlot.OFF_HAND) return false;
        if (!event.getAction().isLeftClick()) return false;
        if (!player.isSneaking()) return false;

        return true;
    }
}
