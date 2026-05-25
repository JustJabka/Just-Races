package justjabka.JustRaces.Configs.Ability;

import org.bukkit.configuration.file.FileConfiguration;

public class AzaleaCamouflageAbilityConfig {
    public final long cooldown;
    public final int activationTime;
    public final int regenerationAmplifier;
    public final double damageMultiplier;

    public AzaleaCamouflageAbilityConfig(FileConfiguration config) {
        this.cooldown = config.getLong("cooldown", 60) * 20;
        this.activationTime = config.getInt("activation-time", 2) * 20;
        this.regenerationAmplifier = config.getInt("regeneration-amplifier", 1);
        this.damageMultiplier = config.getDouble("damage-multiplier", 2);
    }
}
