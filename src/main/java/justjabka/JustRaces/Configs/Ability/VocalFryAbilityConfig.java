package justjabka.JustRaces.Configs.Ability;

import org.bukkit.configuration.file.FileConfiguration;

public class VocalFryAbilityConfig {
    public final long cooldown;
    public final int duration;
    public final int foodRequired;
    public final int foodDrained;

    public VocalFryAbilityConfig(FileConfiguration config) {
        this.cooldown = config.getLong("cooldown", 1) * 20;
        this.duration = config.getInt("duration", 3) * 20;
        this.foodRequired = config.getInt("food.required", 8);
        this.foodDrained = config.getInt("food.drained", 4);
    }
}
