package justjabka.WeltenRaces.Configs.Abilities;

import org.bukkit.configuration.file.FileConfiguration;

public class UnfoldWingsConfig {
    public final long cooldown;
    public final int effectDuration;

    public UnfoldWingsConfig(FileConfiguration config) {
        this.cooldown = config.getLong("cooldown", 45) * 20;
        this.effectDuration = config.getInt("effect-duration", 11) * 20;
    }
}
