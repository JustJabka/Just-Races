package justjabka.WeltenRaces.Configs.Ability;

import org.bukkit.configuration.file.FileConfiguration;

public class PoisonousAreaAbilityConfig {
    public final long cooldown;
    public final int fuelDrainAmount;
    public final int effectDuration;
    public final float effectRadius;

    public PoisonousAreaAbilityConfig(FileConfiguration config) {
        this.cooldown = config.getLong("cooldown", 3) * 20;
        this.fuelDrainAmount = config.getInt("fuel-drain-amount", 1);
        this.effectDuration = config.getInt("effect.duration", 3) * 20;
        this.effectRadius = (float) config.getDouble("effect.radius", 10);
    }
}
