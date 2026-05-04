package justjabka.WeltenRaces.Configs.Abilities;

import org.bukkit.configuration.file.FileConfiguration;

public class DamageInversionAbilityConfig {
    public final long cooldown;
    public final double lowerBound;
    public final double upperBound;

    public DamageInversionAbilityConfig(FileConfiguration config) {
        this.cooldown = config.getLong("cooldown", 1) * 20;
        this.lowerBound = config.getDouble("lower-bound", 3.0);
        this.upperBound = config.getDouble("upper-bound", 30.0);
    }
}
