package justjabka.WeltenRaces.Configs.Ability;

import org.bukkit.configuration.file.FileConfiguration;

public class SwiftSneakAbilityConfig {
    public final long cooldown;
    public final double sneakSpeedBonus;

    public SwiftSneakAbilityConfig(FileConfiguration config) {
        this.cooldown = config.getLong("cooldown", 10) * 20;
        this.sneakSpeedBonus = config.getDouble("sneak-speed-bonus", 0.45);
    }
}
