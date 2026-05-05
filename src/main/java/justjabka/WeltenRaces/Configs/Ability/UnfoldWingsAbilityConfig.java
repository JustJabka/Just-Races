package justjabka.WeltenRaces.Configs.Ability;

import org.bukkit.configuration.file.FileConfiguration;

public class UnfoldWingsAbilityConfig {
    public final long cooldown;
    public final double jumpStrength;

    public UnfoldWingsAbilityConfig(FileConfiguration config) {
        this.cooldown = config.getLong("cooldown", 45) * 20;
        this.jumpStrength = config.getDouble("jump-strength", 2);
    }
}
