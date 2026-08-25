package justjabka.JustRacesShowcase.Runnables.Race;

import justjabka.JustRacesShowcase.DataProvider.RaceProvider;
import justjabka.JustRaces.Managers.AbilityManager;
import justjabka.JustRaces.Managers.AttributeManager;
import justjabka.JustRaces.Runnables.Generic.BaseRaceRunnable;
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

import static justjabka.JustRacesShowcase.Abilities.TrueFormAbility.TRUE_FORM_KEY;

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
        double temperatureBuffMovementSpeedBonus = getConfigDouble("temperature_buff", "movement_speed_bonus");
        double temperatureBuffJumpStrengthBonus = getConfigDouble("temperature_buff", "jump_strength_bonus");

        return Map.of(
                Attribute.MOVEMENT_SPEED, new AttributeModifier(getRaceKey(), temperatureBuffMovementSpeedBonus, AttributeModifier.Operation.ADD_NUMBER),
                Attribute.JUMP_STRENGTH, new AttributeModifier(getRaceKey(), temperatureBuffJumpStrengthBonus, AttributeModifier.Operation.ADD_NUMBER)
        );
    }

    private boolean isInWarmBiome(Location location) {
        boolean hasStorm = location.getWorld().hasStorm();
        if (hasStorm) return true;

        double temperatureBuffLowerBound = getConfigDouble("temperature_buff", "lower_bound");
        double temperatureBuffUpperBound = getConfigDouble("temperature_buff", "upper_bound");

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
