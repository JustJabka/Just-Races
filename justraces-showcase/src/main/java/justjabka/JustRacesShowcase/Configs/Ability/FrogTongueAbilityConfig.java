package justjabka.JustRacesShowcase.Configs.Ability;

import org.bukkit.configuration.file.FileConfiguration;

public class FrogTongueAbilityConfig {
    public final long cooldownDefault;
    public final long cooldownEntity;
    public final long cooldownBlock;
    public final float size;

    public FrogTongueAbilityConfig(FileConfiguration config) {
        this.cooldownDefault = config.getLong("cooldown.default");
        this.cooldownEntity = config.getLong("cooldown.entity");
        this.cooldownBlock = config.getLong("cooldown.block");
        this.size = (float) (config.getDouble("size"));
    }
}
