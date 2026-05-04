package justjabka.WeltenRaces.Configs.Race;

import org.bukkit.configuration.file.FileConfiguration;

public class PhantomRaceConfig {
    public final double membraneHealAmount;
    public final int meatBonusFoodAmount;
    public final int meatBonusRegenerationDuration;
    public final int insomniaDamageBonus;
    public final int helmetDurabilityDrain;
    public final double nightMovementSpeedBonus;

    public PhantomRaceConfig(FileConfiguration config) {
        this.membraneHealAmount = config.getDouble("membrane-heal-amount", 1);
        this.meatBonusFoodAmount = config.getInt("meat-bonus.food-amount", 5);
        this.meatBonusRegenerationDuration = config.getInt("meat-bonus.regeneration-duration", 2) * 20;
        this.insomniaDamageBonus = config.getInt("insomnia-damage-bonus", 3);
        this.helmetDurabilityDrain = config.getInt("helmet-durability-drain", 1);
        this.nightMovementSpeedBonus = config.getDouble("night-movement-speed-bonus", 0.02);
    }
}