package justjabka.WeltenRaces.Configs.Abilities;

import org.bukkit.configuration.file.FileConfiguration;

public class DamageInversionConfig {
    public final long cooldown;

    public DamageInversionConfig(FileConfiguration config) {
        this.cooldown = config.getLong("cooldown", 1) * 20;
    }
}
