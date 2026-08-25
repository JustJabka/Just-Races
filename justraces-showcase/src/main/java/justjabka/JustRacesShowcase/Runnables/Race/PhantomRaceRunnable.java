package justjabka.JustRacesShowcase.Runnables.Race;

import justjabka.JustRacesShowcase.DataProvider.RaceProvider;
import justjabka.JustRaces.Runnables.Generic.BaseRaceRunnable;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Set;

public class PhantomRaceRunnable extends BaseRaceRunnable {
    private static final Set<PotionEffect> NIGHT_TIME_EFFECTS = Set.of(
            new PotionEffect(PotionEffectType.NIGHT_VISION, PotionEffect.INFINITE_DURATION, 0, false, false, false)
    );

    @Override
    public NamespacedKey getRaceKey() {
        return RaceProvider.PHANTOM;
    }

    @Override
    public void onTick(Player player) {
        World world = player.getWorld();
        Location location = player.getLocation();

        boolean isDayTime = player.getWorld().isDayTime();
        boolean isClearWeather = world.isClearWeather();
        boolean canSeeSky = location.getY() >= world.getHighestBlockYAt(location);
        boolean isDark = location.getBlock().getLightLevel() <= 7;

        boolean hasFireResistance = player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE);
        boolean isInWater = player.isInWater();
        boolean isInvulnerable = player.getGameMode().isInvulnerable();

        boolean hasHelmet = !player.getEquipment().getHelmet().isEmpty();

        boolean immuneToBurn = hasFireResistance || isInWater || isInvulnerable;
        boolean willBurn = isDayTime && isClearWeather && canSeeSky && !immuneToBurn;
        boolean willReceiveBuff = !isDayTime || isDark;

        checkTime(player, willBurn, hasHelmet, willReceiveBuff);
    }

    private void checkTime(Player player, boolean willBurn, boolean hasHelmet, boolean willReceiveBuff) {
        if (willBurn) {
            if (handleHelmetLogic(player, hasHelmet)) return;

            player.setFireTicks(40);
        }

        applyBuffs(player, willReceiveBuff);
    }

    private boolean handleHelmetLogic(Player player, boolean hasHelmet) {
        if (!hasHelmet) return false;

        int helmetDurabilityDrain = getConfigInt("helmet_durability_drain");

        player.getEquipment().getHelmet().damage(helmetDurabilityDrain, player);
        return true;
    }

    private void applyBuffs(Player player, boolean willReceiveBuff) {
        AttributeInstance movementSpeedInstance = player.getAttribute(Attribute.MOVEMENT_SPEED);
        if (movementSpeedInstance == null) return;

        boolean hasModifier = movementSpeedInstance.getModifier(getRaceKey()) != null;

        if (willReceiveBuff) {
            NIGHT_TIME_EFFECTS.forEach(player::addPotionEffect);

            if (hasModifier) return;

            double nightMovementSpeedBonus = getConfigDouble("night_movement_speed_bonus");

            AttributeModifier modifier = new AttributeModifier(
                    getRaceKey(),
                    nightMovementSpeedBonus,
                    AttributeModifier.Operation.ADD_NUMBER
            );

            movementSpeedInstance.addModifier(modifier);
        } else {
            NIGHT_TIME_EFFECTS.forEach(effect -> player.removePotionEffect(effect.getType()));

            if (!hasModifier) return;
            movementSpeedInstance.removeModifier(getRaceKey());
        }
    }
}
