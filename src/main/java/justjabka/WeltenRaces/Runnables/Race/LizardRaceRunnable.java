package justjabka.WeltenRaces.Runnables.Race;

import justjabka.WeltenRaces.DataProvider.RaceProvider;
import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.Managers.AttributeManager;
import justjabka.WeltenRaces.Runnables.Generic.BaseRaceRunnable;
import org.apache.commons.lang3.Range;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.Set;

import static justjabka.WeltenRaces.Abilities.TrueFormAbility.TRUE_FORM_KEY;

public class LizardRaceRunnable extends BaseRaceRunnable {
    private static final Set<PotionEffect> netherDebuffs = Set.of(
            new PotionEffect(PotionEffectType.SLOWNESS, 40, 0, false, false, false),
            new PotionEffect(PotionEffectType.MINING_FATIGUE, 40, 0, false, false, false)
    );

    @Override
    public NamespacedKey getRaceKey() {
        return RaceProvider.LIZARD;
    }

    @Override
    public void onTick(Player player) {
        Location location = player.getLocation();
        World world = location.getWorld();

        giveWarmBiomesBuff(player, location);
        giveNetherDebuff(player, world);
    }

    private void giveWarmBiomesBuff(Player player, Location location) {
        Map<Attribute, AttributeModifier> warmBiomesBuffs = getWarmBiomesModifiers();

        boolean hasModifiers = AttributeManager.hasModifiers(player, warmBiomesBuffs);

        boolean giveBuff = isInWarmBiome(location) && !hasModifiers;
        boolean clearBuff = !isInWarmBiome(location) && hasModifiers;

        if (giveBuff) {
            AttributeManager.addModifiers(player, warmBiomesBuffs);
        } else if (clearBuff) {
            AttributeManager.removeModifiers(player, warmBiomesBuffs);
        }
    }

    private Map<Attribute, AttributeModifier> getWarmBiomesModifiers() {
        double temperatureBuffMovementSpeedBonus = getConfig().node("temperature-buff", "movement-speed-bonus").getDouble();
        double temperatureBuffJumpStrengthBonus = getConfig().node("temperature-buff", "jump-strength-bonus").getDouble();

        return Map.of(
                Attribute.MOVEMENT_SPEED, new AttributeModifier(getRaceKey(), temperatureBuffMovementSpeedBonus, AttributeModifier.Operation.ADD_NUMBER),
                Attribute.JUMP_STRENGTH, new AttributeModifier(getRaceKey(), temperatureBuffJumpStrengthBonus, AttributeModifier.Operation.ADD_NUMBER)
        );
    }

    private boolean isInWarmBiome(Location location) {
        boolean hasStorm = location.getWorld().hasStorm();
        if (hasStorm) return true;

        double temperatureBuffLowerBound = getConfig().node("temperature-buff", "lower-bound").getDouble();
        double temperatureBuffUpperBound = getConfig().node("temperature-buff", "upper-bound").getDouble();

        double temperature = location.getBlock().getTemperature();
        Range<Double> buffBoundary = Range.between(temperatureBuffLowerBound, temperatureBuffUpperBound);

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
