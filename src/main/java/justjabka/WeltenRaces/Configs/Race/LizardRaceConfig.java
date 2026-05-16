package justjabka.WeltenRaces.Configs.Race;

import org.bukkit.configuration.file.FileConfiguration;

public class LizardRaceConfig {
    public final double vulnerableDamageMultiplier;
    public final double resistantDamageMultiplier;

    public final double temperatureBuffLowerBound;
    public final double temperatureBuffUpperBound;

    public final double temperatureBuffMovementSpeedBonus;
    public final double temperatureBuffJumpStrengthBonus;

    public LizardRaceConfig (FileConfiguration config) {
        this.vulnerableDamageMultiplier = config.getDouble("damage-multiplier.vulnerable", 1.05);
        this.resistantDamageMultiplier = config.getDouble("damage-multiplier.resistant", 0.8);
        this.temperatureBuffLowerBound = config.getDouble("temperature-buff.lower-bound", 0.8);
        this.temperatureBuffUpperBound = config.getDouble("temperature-buff.upper-bound", 1.5);
        this.temperatureBuffMovementSpeedBonus = config.getDouble("temperature-buff.movement-speed-bonus", 0.13);
        this.temperatureBuffJumpStrengthBonus = config.getDouble("temperature-buff.jump-strength-bonus", 0.5);
    }
}
