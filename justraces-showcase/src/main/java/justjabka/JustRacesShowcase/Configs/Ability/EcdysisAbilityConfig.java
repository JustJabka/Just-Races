package justjabka.JustRacesShowcase.Configs.Ability;

import org.bukkit.configuration.file.FileConfiguration;

public class EcdysisAbilityConfig {
    public final long cooldown;
    public final long effectDuration;
    public final float explosionPowerNormal;
    public final float explosionPowerSuicide;
    public final float durabilityPercentPerUse;
    public final int maxChainAmount;
    public final double suicideDurabilityPercent;

    public EcdysisAbilityConfig(FileConfiguration config) {
        this.cooldown = config.getLong("cooldown", 9) * 20;
        this.effectDuration = config.getLong("effect-duration", 11) * 20;
        this.explosionPowerNormal = (float) config.getDouble("explosion-power.normal", 6);
        this.explosionPowerSuicide = (float) config.getDouble("explosion-power.suicide", 7);
        this.durabilityPercentPerUse = (float) config.getDouble("durability-percent-per-use", 0.25);
        this.maxChainAmount = config.getInt("max-chain-amount", 6);
        this.suicideDurabilityPercent = config.getDouble("suicide-durability-percent", 0.1);
    }
}
