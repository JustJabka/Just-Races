package justjabka.WeltenRaces.Configs.Race;

import org.bukkit.configuration.file.FileConfiguration;

public class ArmatConfig {
    public final double vulnerableMultiplier;
    public final double sinkGravity;

    public final double miningBonusMax;
    public final double miningBonusStep;

    public final double maxDodgeChance;
    public final double parryDamagePercent;
    public final int parryArmorPenalty;

    public final double reductionStart;
    public final double reductionMultiplier;

    public final double effectDurationMultiplier;

    public final double absoluteDamageAmount;
    public final float absoluteDamageCooldown;

    public ArmatConfig(FileConfiguration config) {
        // Base
        this.vulnerableMultiplier = config.getDouble("vulnerable-damage-multiplier", 1.5);
        this.sinkGravity = config.getDouble("sink-gravity-value", 0.32);

        // Copper Armor
        this.miningBonusMax = config.getDouble("mining-bonus.upper-bound", 3.0);
        this.miningBonusStep = config.getDouble("mining-bonus.step", 0.25);

        // Chainmail Armor
        this.maxDodgeChance = config.getDouble("damage-dodge.max-chance", 0.5);
        this.parryDamagePercent = config.getDouble("damage-dodge.parry.damage-percent", 0.6);
        this.parryArmorPenalty = config.getInt("damage-dodge.parry.armor-penalty-Penalty", 1);

        // Iron Armor
        this.reductionStart = config.getDouble("damage-reduction.starting-point", 8.0);
        this.reductionMultiplier = config.getDouble("damage-reduction.damage-multiplier", 0.8);

        // Golden Armor
        this.effectDurationMultiplier = config.getDouble("alchemy.effect-duration-multiplier", 1.25);

        // Diamond Armor
        this.absoluteDamageAmount = config.getDouble("absolute-damage.amount", 1.0);
        this.absoluteDamageCooldown = (float) config.getDouble("absolute-damage.min-attack-cooldown", 1.0);
    }
}