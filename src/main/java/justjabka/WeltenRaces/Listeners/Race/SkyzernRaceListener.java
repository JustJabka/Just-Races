package justjabka.WeltenRaces.Listeners.Race;

import justjabka.WeltenRaces.Configs.Race.SkyzernRaceConfig;
import justjabka.WeltenRaces.DataProvider.RaceProvider;
import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SkyzernRaceListener implements Listener {
    private final SkyzernRaceConfig config;

    public SkyzernRaceListener(SkyzernRaceConfig config) {
        this.config = config;
    }

    private static final NamespacedKey CELESTIAL_ORIGIN_KEY = new NamespacedKey(WeltenRaces.NAMESPACE, "celestial_origin");

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!RaceManager.isRace(player, RaceProvider.SKYZERN)) return;
        clearCelestialOriginBonus(player);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        if (!RaceManager.isRace(player, RaceProvider.SKYZERN)) return;

        if (event.isSneaking()) {
            giveCelestialOriginBonus(player);
        } else {
            clearCelestialOriginBonus(player);
        }
    }

    private static void giveCelestialOriginBonus(Player player) {
        PotionEffect onSneakEffect = new PotionEffect(
                PotionEffectType.SLOW_FALLING,
                PotionEffect.INFINITE_DURATION,
                0,
                false,
                false,
                false
        );

        player.addPotionEffect(onSneakEffect);
    }

    private static void clearCelestialOriginBonus(Player player) {
        PotionEffect currentEffect = player.getPotionEffect(PotionEffectType.SLOW_FALLING);

        if (currentEffect == null) return;
        if (!currentEffect.isInfinite()) return;

        player.removePotionEffect(PotionEffectType.SLOW_FALLING);
    }

    @EventHandler
    public void onHotbarChange(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();

        if (!RaceManager.isRace(player, RaceProvider.SKYZERN)) return;

        AttributeInstance attackKnockbackInstance = player.getAttribute(Attribute.ATTACK_KNOCKBACK);
        if (attackKnockbackInstance == null) return;

        boolean hasAbilitySlotSelected = event.getNewSlot() == AbilityManager.getActivationSlot();
        boolean hasModifier = attackKnockbackInstance.getModifier(CELESTIAL_ORIGIN_KEY) != null;
        boolean willReceiveBuff = hasAbilitySlotSelected && !hasModifier;

        if (willReceiveBuff) {
            AttributeModifier modifier = new AttributeModifier(
                    CELESTIAL_ORIGIN_KEY,
                    config.additionalKnockbackValue,
                    AttributeModifier.Operation.ADD_NUMBER
            );

            attackKnockbackInstance.addModifier(modifier);
        } else  {
            attackKnockbackInstance.removeModifier(CELESTIAL_ORIGIN_KEY);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity victim)) return;
        if (!(event.getDamager() instanceof Player attacker)) return;

        if (!RaceManager.isRace(attacker, RaceProvider.SKYZERN)) return;

        AttributeInstance attackKnockbackInstance = attacker.getAttribute(Attribute.ATTACK_KNOCKBACK);
        if (attackKnockbackInstance == null) return;

        boolean hasModifier = attackKnockbackInstance.getModifier(CELESTIAL_ORIGIN_KEY) != null;
        if (!hasModifier) return;

        World world = victim.getWorld();

        world.spawnParticle(
                Particle.SMALL_GUST,
                victim.getX(),
                victim.getBoundingBox().getCenterY(),
                victim.getZ(),
                20,
                0.25,
                0.5,
                0.25
        );

        world.playSound(victim.getLocation(), Sound.ENTITY_WIND_CHARGE_WIND_BURST, SoundCategory.PLAYERS, 1, 1);
    }
}