package justjabka.JustRacesShowcase.Configs.Ability;

import org.bukkit.configuration.file.FileConfiguration;

public class PoisonousBiteAbilityConfig {
    public final long cooldown;
    public final int duration;
    public final int foodBonus;

    public PoisonousBiteAbilityConfig(FileConfiguration config) {
        this.cooldown = config.getLong("cooldown", 2) * 20;
        this.duration = config.getInt("duration", 8) * 20;
        this.foodBonus = config.getInt("food-bonus", 1);
    }
}
