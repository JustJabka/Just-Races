package justjabka.JustRaces.Configs.Ability;

import org.bukkit.configuration.file.FileConfiguration;

public class GluttonyExecuteAbilityConfig {
    public final long cooldown;
    public final double smallKillMaxHealth;
    public final int smallKillSecondsToExtend;
    public final double bigKillMaxHealth;
    public final double bigKillRequiredHealth;
    public final int bigKillSecondsToExtend;

    public GluttonyExecuteAbilityConfig(FileConfiguration config) {
        this.cooldown = config.getLong("cooldown", 0) * 20;
        this.smallKillMaxHealth = config.getDouble("small-kill.max-health", 6);
        this.smallKillSecondsToExtend = config.getInt("small-kill.seconds-to-extend", 10);
        this.bigKillMaxHealth = config.getDouble("big-kill.max-health", 26);
        this.bigKillRequiredHealth = config.getDouble("big-kill.required-health", 6);
        this.bigKillSecondsToExtend = config.getInt("big-kill.seconds-to-extend", 20);
    }
}
