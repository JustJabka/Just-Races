package justjabka.WeltenRaces.Runnables.Race;

import justjabka.WeltenRaces.Configs.Race.LizardRaceConfig;
import justjabka.WeltenRaces.Managers.AbilityManager;
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

import static justjabka.WeltenRaces.Abilities.TrueFormAbility.TRUE_FORM_KEY;

public class LizardRaceRunnable extends BukkitRunnable {
    private final LizardRaceConfig config;

    private final Map<Attribute, AttributeModifier> warmBiomesBuffs;
    private final Set<PotionEffect> netherDebuffs;

    private static final NamespacedKey REPTILE_NATURE_KEY = new NamespacedKey(WeltenRaces.NAMESPACE, "reptile_nature");

    public LizardRaceRunnable(LizardRaceConfig config) {
        this.config = config;
        this.warmBiomesBuffs = Map.of(
                Attribute.MOVEMENT_SPEED, new AttributeModifier(REPTILE_NATURE_KEY, config.temperatureBuffMovementSpeedBonus, AttributeModifier.Operation.ADD_NUMBER),
                Attribute.JUMP_STRENGTH, new AttributeModifier(REPTILE_NATURE_KEY, config.temperatureBuffJumpStrengthBonus, AttributeModifier.Operation.ADD_NUMBER)
        );
        this.netherDebuffs = Set.of(
                new PotionEffect(PotionEffectType.SLOWNESS, 40, 0, false, false, false),
                new PotionEffect(PotionEffectType.MINING_FATIGUE, 40, 0, false, false, false)
        );
    }

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

    private boolean isInWarmBiome(Location location) {
        boolean hasStorm = location.getWorld().hasStorm();
        if (hasStorm) return true;

        double temperature = location.getBlock().getTemperature();
        Range<Double> buffBoundary = Range.between(config.temperatureBuffLowerBound, config.temperatureBuffUpperBound);

        return buffBoundary.contains(temperature);
    }

    private void giveNetherDebuff(Player player, World world) {
        boolean isInTheNether = world.getEnvironment() == World.Environment.NETHER;
        boolean hasFireResistance = player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE);
        boolean isInTrueForm = AbilityManager.isAbilityActive(player, TRUE_FORM_KEY);

        boolean immuneToDebuff = hasFireResistance || isInTrueForm;
        boolean willReceiveDebuff = isInTheNether && !immuneToDebuff;

        if (!willReceiveDebuff) return;

        netherDebuffs.forEach(player::addPotionEffect);
    }
}
