package justjabka.WeltenRaces.Configs.Abilities;

import org.bukkit.configuration.file.FileConfiguration;

public class UnfoldWingsConfig {
    public final long cooldown;
    public final double jumpStrength;

    public UnfoldWingsConfig(FileConfiguration config) {
        this.cooldown = config.getLong("cooldown", 45) * 20;
        this.jumpStrength = config.getDouble("jump-strength", 2);
    }
}
