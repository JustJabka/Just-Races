package justjabka.WeltenRaces.Configs.Race;

import org.bukkit.configuration.file.FileConfiguration;

public class SkyzernRaceConfig {
    public final double damageBonusPerStep;
    public final double damageBonusStep;

    public SkyzernRaceConfig(FileConfiguration config) {
        this.damageBonusPerStep = config.getDouble("damage-bonus.value-per-step", 0.1);
        this.damageBonusStep = config.getDouble("damage-bonus.step", 5.0);
    }
}