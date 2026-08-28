package justjabka.JustRacesShowcase.Configs.Ability;

import org.bukkit.configuration.file.FileConfiguration;

public class FrogTongueAbilityConfig {
    public final long cooldown;
    public final float size;

    public FrogTongueAbilityConfig(FileConfiguration config) {
        this.cooldown = config.getLong("cooldown");
        this.size = (float) (config.getDouble("size"));
    }
}
