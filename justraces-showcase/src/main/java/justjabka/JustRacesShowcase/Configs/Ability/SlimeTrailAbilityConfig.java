package justjabka.JustRacesShowcase.Configs.Ability;

import org.bukkit.configuration.file.FileConfiguration;

public class SlimeTrailAbilityConfig {
    public final long cooldown;
    public final long duration;

    public final float trailRadius;
    public final int trailDuration;
    public final int trailEffectsDuration;

    public SlimeTrailAbilityConfig(FileConfiguration config) {
        this.cooldown = config.getLong("cooldown");
        this.duration = config.getLong("duration");


        this.trailRadius = (float) config.getDouble("trail.radius");
        this.trailDuration = config.getInt("trail.duration");
        this.trailEffectsDuration = config.getInt("trail.effects-duration");
    }
}
