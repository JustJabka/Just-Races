package justjabka.WeltenRaces.Runnables;

import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.Types.Race;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

public class PhantomRaceRunnable extends BukkitRunnable {
    private static final NamespacedKey DREAMCATCHER_KEY = new NamespacedKey(WeltenRaces.NAMESPACE, "dreamcatcher");

    private static final double MOVEMENT_SPEED_BONUS = 0.02;
    private static final int NIGHT_TIME_EFFECTS_DURATION = -1;

    private static final Set<PotionEffect> NIGHT_TIME_EFFECTS = Set.of(
            new PotionEffect(PotionEffectType.NIGHT_VISION, NIGHT_TIME_EFFECTS_DURATION, 0, false, false, false)
    );

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (RaceManager.getRace(player) != Race.PHANTOM) continue;

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
    }

    private static void checkTime(Player player, boolean willBurn, boolean hasHelmet, boolean willReceiveBuff) {
        if (willBurn) {
            if (hasHelmet) {
                player.getEquipment().getHelmet().damage(1, player);
                return;
            }

            player.setFireTicks(40);
        }

        AttributeInstance movementSpeedInstance = player.getAttribute(Attribute.MOVEMENT_SPEED);
        if (movementSpeedInstance == null) return;

        boolean hasModifier = movementSpeedInstance.getModifier(DREAMCATCHER_KEY) != null;

        if (willReceiveBuff && !hasModifier) {
            NIGHT_TIME_EFFECTS.forEach(player::addPotionEffect);

            AttributeModifier modifier = new AttributeModifier(
                    DREAMCATCHER_KEY,
                    MOVEMENT_SPEED_BONUS,
                    AttributeModifier.Operation.ADD_NUMBER
            );

            movementSpeedInstance.addModifier(modifier);
        } else if (!willReceiveBuff && hasModifier) {
            player.getActivePotionEffects().forEach(effect -> {
                if (!(NIGHT_TIME_EFFECTS.contains(effect))) return;
                player.removePotionEffect(effect.getType());
            });
            movementSpeedInstance.removeModifier(DREAMCATCHER_KEY);
        }
    }
}
