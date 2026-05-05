package justjabka.WeltenRaces.Configs.Ability;

import org.bukkit.configuration.file.FileConfiguration;

public class WildHuntAbilityConfig {
    public final long cooldown;
    public final double radius;

    public WildHuntAbilityConfig(FileConfiguration config) {
        this.cooldown = config.getLong("cooldown", 120) * 20;
        this.radius = config.getDouble("radius", 120);
    }
}
