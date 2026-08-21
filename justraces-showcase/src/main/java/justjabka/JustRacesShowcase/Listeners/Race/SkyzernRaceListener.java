package justjabka.JustRacesShowcase.Listeners.Race;

import justjabka.JustRacesShowcase.DataProvider.RaceProvider;
import justjabka.JustRaces.Listeners.Generic.BaseRaceListener;
import justjabka.JustRaces.Managers.AbilityManager;
import justjabka.JustRaces.Managers.RaceManager;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SkyzernRaceListener extends BaseRaceListener {
    @Override
    public NamespacedKey getRaceKey() {
        return RaceProvider.SKYZERN;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!isRequiredRace(player)) return;
        clearCelestialOriginBonus(player);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        if (!isRequiredRace(player)) return;

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
        boolean hasModifier = attackKnockbackInstance.getModifier(getRaceKey()) != null;
        boolean willReceiveBuff = hasAbilitySlotSelected && !hasModifier;

        if (willReceiveBuff) {
            double additionalKnockbackValue = getConfig().node("additional_knockback_value").getDouble();

            AttributeModifier modifier = new AttributeModifier(
                    getRaceKey(),
                    additionalKnockbackValue,
                    AttributeModifier.Operation.ADD_NUMBER
            );

            attackKnockbackInstance.addModifier(modifier);
        } else {
            attackKnockbackInstance.removeModifier(getRaceKey());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity victim)) return;
        if (!(event.getDamager() instanceof Player attacker)) return;

        if (!RaceManager.isRace(attacker, RaceProvider.SKYZERN)) return;

        AttributeInstance attackKnockbackInstance = attacker.getAttribute(Attribute.ATTACK_KNOCKBACK);
        if (attackKnockbackInstance == null) return;

        boolean hasModifier = attackKnockbackInstance.getModifier(getRaceKey()) != null;
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