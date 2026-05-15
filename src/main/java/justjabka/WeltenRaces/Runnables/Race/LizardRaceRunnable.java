package justjabka.WeltenRaces.Runnables.Race;

import justjabka.WeltenRaces.Managers.AttributeManager;
import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.Types.Race;
import justjabka.WeltenRaces.WeltenRaces;
import org.apache.commons.lang3.Range;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.Set;

public class LizardRaceRunnable extends BukkitRunnable {
    private static final NamespacedKey REPTILE_NATURE_KEY = new NamespacedKey(WeltenRaces.NAMESPACE, "reptile_nature");

    private static final double minBuffTemperature = 0.8;
    private static final double maxBuffTemperature = 1.8;

    private static final Map<Attribute, AttributeModifier> warmBiomesBuffs = Map.of(
            Attribute.MOVEMENT_SPEED, new AttributeModifier(
                    REPTILE_NATURE_KEY,
                    0.13,
                    AttributeModifier.Operation.ADD_NUMBER
            ),
            Attribute.JUMP_STRENGTH, new AttributeModifier(
                    REPTILE_NATURE_KEY,
                    0.5,
                    AttributeModifier.Operation.ADD_NUMBER
            )
    );
    private static final Set<PotionEffect> netherDebuffs = Set.of(
            new PotionEffect(PotionEffectType.SLOWNESS, 40, 0, false, false, false),
            new PotionEffect(PotionEffectType.MINING_FATIGUE, 40, 0, false, false, false)
    );

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (RaceManager.getRace(player) != Race.LIZARD) continue;

            Location location = player.getLocation();
            World world = location.getWorld();

            giveWarmBiomesBuff(player, location);
            giveNetherDebuff(player, world);
        }
    }

    private void giveWarmBiomesBuff(Player player, Location location) {
        boolean hasModifiers = AttributeManager.hasModifiers(player, warmBiomesBuffs);

        boolean giveBuff = isInWarmBiome(location) && !hasModifiers;
        boolean clearBuff = !isInWarmBiome(location) && hasModifiers;

        if (giveBuff) {
            AttributeManager.addModifiers(player, warmBiomesBuffs);
        } else if (clearBuff) {
            AttributeManager.removeModifiers(player, warmBiomesBuffs);
        }
    }

    private static boolean isInWarmBiome(Location location) {
        boolean hasStorm = location.getWorld().hasStorm();
        if (hasStorm) return true;

        double temperature = location.getBlock().getTemperature();
        Range<Double> buffBoundary = Range.between(minBuffTemperature, maxBuffTemperature);

        return buffBoundary.contains(temperature);
    }

    private static void giveNetherDebuff(Player player, World world) {
        boolean isInTheNether = world.getEnvironment() == World.Environment.NETHER;
        boolean hasFireResistance = player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE);

        boolean willReceiveDebuff = isInTheNether && !hasFireResistance;
        if (!willReceiveDebuff) return;

        netherDebuffs.forEach(player::addPotionEffect);
    }
}
