package justjabka.WeltenRaces.Runnables.Race;

import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.Types.Race;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SkyzernRaceRunnable extends BukkitRunnable {
    private static final NamespacedKey CELESTIAL_ORIGIN_KEY = new NamespacedKey(WeltenRaces.NAMESPACE, "celestial_origin");

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (RaceManager.getRace(player) != Race.SKYZERN) return;

            applyCelestialOriginBonus(player);
        }
    }

    private static void applyCelestialOriginBonus(Player player) {
        AttributeInstance attackDamageInstance = player.getAttribute(Attribute.ATTACK_DAMAGE);
        if (attackDamageInstance == null) return;

        double bonus = calcCelestialOriginBonus(player);

        // Delete old attribute
        attackDamageInstance.removeModifier(CELESTIAL_ORIGIN_KEY);

        // Apply new attribute
        AttributeModifier modifier = new AttributeModifier(
                CELESTIAL_ORIGIN_KEY,
                bonus,
                AttributeModifier.Operation.ADD_NUMBER
        );

        attackDamageInstance.addModifier(modifier);
    }

    private static double calcCelestialOriginBonus(Player player) {
        double currentHeight = player.getY();
        double baseHeight = player.getWorld().getSeaLevel();
        double step = 0.1 / 5.0;

        return (currentHeight - baseHeight) * step;
    }
}
