package justjabka.WeltenRaces.Configs.Abilities;

import org.bukkit.configuration.file.FileConfiguration;

public class WildHuntConfig {
    public final long cooldown;
    public final double radius;

    public WildHuntConfig(FileConfiguration config) {
        this.cooldown = config.getLong("cooldown", 120) * 20;
        this.radius = config.getDouble("radius", 120);
    }
}
