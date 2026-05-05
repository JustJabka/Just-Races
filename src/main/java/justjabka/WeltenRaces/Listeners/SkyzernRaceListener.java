package justjabka.WeltenRaces.Listeners;

import justjabka.WeltenRaces.Configs.Race.SkyzernRaceConfig;
import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.Types.Race;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SkyzernRaceListener implements Listener {
    private final SkyzernRaceConfig config;

    public SkyzernRaceListener(SkyzernRaceConfig config) {
        this.config = config;
    }

    private static final NamespacedKey CELESTIAL_ORIGIN_KEY = new NamespacedKey(WeltenRaces.NAMESPACE, "celestial_origin");

    @EventHandler(ignoreCancelled = true)
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        if (RaceManager.getRace(player) != Race.SKYZERN) return;

        if (event.isSneaking()) {
            PotionEffect onSneakEffect = new PotionEffect(
                    PotionEffectType.SLOW_FALLING,
                    PotionEffect.INFINITE_DURATION,
                    0,
                    false,
                    false,
                    false
            );

            player.addPotionEffect(onSneakEffect);
        } else {
            PotionEffect currentEffect = player.getPotionEffect(PotionEffectType.SLOW_FALLING);

            if (currentEffect == null) return;
            if (!currentEffect.isInfinite()) return;

            player.removePotionEffect(PotionEffectType.SLOW_FALLING);
        }
    }

    @EventHandler
    public void onHotbarChange(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();

        if (RaceManager.getRace(player) != Race.SKYZERN) return;

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

        if (RaceManager.getRace(attacker) != Race.SKYZERN) return;

        AttributeInstance attackKnockbackInstance = attacker.getAttribute(Attribute.ATTACK_KNOCKBACK);
        if (attackKnockbackInstance == null) return;

        boolean hasModifier = attackKnockbackInstance.getModifier(CELESTIAL_ORIGIN_KEY) != null;
        if (!hasModifier) return;

        victim.getWorld().spawnParticle(
                Particle.GUST_EMITTER_SMALL,
                victim.getX(),
                victim.getBoundingBox().getCenterY(),
                victim.getZ(),
                1
        );

        victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_WIND_CHARGE_WIND_BURST, SoundCategory.PLAYERS, 1, 1);
    }
}