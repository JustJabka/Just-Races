package justjabka.WeltenRaces.Configs.Abilities;

import org.bukkit.configuration.file.FileConfiguration;

public class PredatorVisionConfig {
    public final long cooldown;
    public final int effectDuration;
    public final int radius;

    public PredatorVisionConfig(FileConfiguration config) {
        this.cooldown = config.getLong("cooldown", 15) * 20;
        this.effectDuration = config.getInt("effect-duration", 10) * 20;
        this.radius = config.getInt("radius", 15);
    }
}
