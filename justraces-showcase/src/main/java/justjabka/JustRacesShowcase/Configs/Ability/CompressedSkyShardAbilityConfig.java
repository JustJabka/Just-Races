package justjabka.JustRacesShowcase.Configs.Ability;

import org.bukkit.configuration.file.FileConfiguration;

public class CompressedSkyShardAbilityConfig {
    public final long cooldown;
    public final int radius;
    public final int effectDuration;
    public final float effectRadius;

    public CompressedSkyShardAbilityConfig(FileConfiguration config) {
        this.cooldown = config.getLong("cooldown", 45) * 20;
        this.radius = config.getInt("radius", 16);
        this.effectDuration = config.getInt("effect.duration", 11) * 20;
        this.effectRadius = (float) config.getDouble("effect.radius", 2.5);
    }
}