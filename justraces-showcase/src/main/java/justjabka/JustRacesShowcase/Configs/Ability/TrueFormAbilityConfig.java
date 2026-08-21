package justjabka.JustRacesShowcase.Configs.Ability;

import org.bukkit.configuration.file.FileConfiguration;

public class TrueFormAbilityConfig {
    public final long cooldown;
    public final int duration;
    public final int debuffDuration;
    public final double scaleBonus;
    public final double maxHealthBonus;

    public TrueFormAbilityConfig(FileConfiguration config) {
        this.cooldown = config.getLong("cooldown", 180) * 20;
        this.duration = config.getInt("duration", 30) * 20;
        this.debuffDuration = config.getInt("debuff-duration", 10) * 20;
        this.scaleBonus = config.getDouble("scale-bonus", 0.3);
        this.maxHealthBonus = config.getDouble("max-health-bonus", 14);
    }
}
