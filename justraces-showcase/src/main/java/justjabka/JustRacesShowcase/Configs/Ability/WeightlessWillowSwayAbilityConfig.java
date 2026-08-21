package justjabka.JustRacesShowcase.Configs.Ability;

import org.bukkit.configuration.file.FileConfiguration;

public class WeightlessWillowSwayAbilityConfig {
    public final long cooldown;
    public final int effectDuration;

    public WeightlessWillowSwayAbilityConfig(FileConfiguration config) {
        this.cooldown = config.getLong("cooldown", 10) * 20;
        this.effectDuration = config.getInt("effect-duration", 11) * 20;
    }
}
