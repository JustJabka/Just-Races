package justjabka.WeltenRaces.Configs.Abilities;

import org.bukkit.configuration.file.FileConfiguration;

public class EcdysisAbilityConfig {
    public final long cooldown;
    public final int effectDuration;
    public final int durabilityAfterUse;
    public final double suicideDurabilityPercent;

    public EcdysisAbilityConfig(FileConfiguration config) {
        this.cooldown = config.getLong("cooldown", 10) * 20;
        this.effectDuration = config.getInt("effect-duration", 11) * 20;
        this.durabilityAfterUse = config.getInt("durability-after-use", 1);
        this.suicideDurabilityPercent = config.getDouble("suicide-durability-percent", 0.1);
    }
}
