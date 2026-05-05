package justjabka.WeltenRaces.Configs.Race;

import org.bukkit.configuration.file.FileConfiguration;

public class SkyzernRaceConfig {
    public final double damageBonusPerStep;
    public final double damageBonusStep;
    public final double additionalKnockbackValue;

    public SkyzernRaceConfig(FileConfiguration config) {
        this.damageBonusPerStep = config.getDouble("damage-bonus.value-per-step", 0.1);
        this.damageBonusStep = config.getDouble("damage-bonus.step", 5.0);
        this.additionalKnockbackValue = config.getDouble("additional-knockback-value", 1.0);
    }
}