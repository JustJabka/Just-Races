package justjabka.JustRacesShowcase.Configs.Ability;

import org.bukkit.configuration.file.FileConfiguration;

public class PoisonousSplitAbilityConfig {
    public final long cooldown;
    public final int effectDuration;
    public final int effectAmplifier;
    public final double projectileDamage;

    public PoisonousSplitAbilityConfig(FileConfiguration config) {
        this.cooldown = config.getLong("cooldown", 5) * 20;
        this.effectDuration = config.getInt("effect.duration", 10) * 20;
        this.effectAmplifier = config.getInt("effect.amplifier", 1);
        this.projectileDamage = config.getDouble("projectile-damage", 4);
    }
}
