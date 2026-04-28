package justjabka.WeltenRaces.Runnables;

import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.Types.Race;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

public class PhantomRaceRunnable extends BukkitRunnable {
    private final static Set<PotionEffect> NIGHT_TIME_EFFECTS = Set.of(
            new PotionEffect(PotionEffectType.NIGHT_VISION, 400, 0, false, false, false),
            new PotionEffect(PotionEffectType.SPEED, 400, 0, false, false, true)
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

            boolean willBurn = isDayTime && isClearWeather && canSeeSky;
            boolean hasHelmet = !player.getEquipment().getHelmet().isEmpty();

            checkTime(player, willBurn, hasHelmet);
        }
    }

    private static void checkTime(Player player, boolean willBurn, boolean hasHelmet) {
        if (willBurn) {
            if (hasHelmet) {
                player.getEquipment().getHelmet().damage(1, player);
                return;
            }

            player.setFireTicks(40);
        } else {
            NIGHT_TIME_EFFECTS.forEach(player::addPotionEffect);
        }
    }
}
