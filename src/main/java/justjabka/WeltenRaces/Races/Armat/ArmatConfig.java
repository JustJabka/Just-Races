package justjabka.WeltenRaces.Races.Armat;

import org.bukkit.configuration.file.FileConfiguration;

public class ArmatConfig {
    public final double vulnerableMultiplier;
    public final double sinkGravity;

    public final double inversionMin;
    public final double inversionMax;

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
        this.vulnerableMultiplier = config.getDouble("Races.Armat.Vulnerable-Damage-Multiplier", 1.5);
        this.sinkGravity = config.getDouble("Races.Armat.Sink-Gravity-Value", 0.32);

        // Leather Armor
        this.inversionMin = config.getDouble("Races.Armat.Damage-Inversion.Lower-Bound", 3.0);
        this.inversionMax = config.getDouble("Races.Armat.Damage-Inversion.Upper-Bound", 30.0);

        // Copper Armor
        this.miningBonusMax = config.getDouble("Races.Armat.Mining-Bonus.Upper-Bound", 3.0);
        this.miningBonusStep = config.getDouble("Races.Armat.Mining-Bonus.Step", 0.25);

        // Chainmail Armor
        this.maxDodgeChance = config.getDouble("Races.Armat.Damage-Dodge.Max-Chance", 0.5);
        this.parryDamagePercent = config.getDouble("Races.Armat.Damage-Dodge.Parry-Damage-Percent", 0.6);
        this.parryArmorPenalty = config.getInt("Races.Armat.Damage-Dodge.Parry-Armor-Penalty", 1);

        // Iron Armor
        this.reductionStart = config.getDouble("Races.Armat.Damage-Reduction.Starting-Point", 8.0);
        this.reductionMultiplier = config.getDouble("Races.Armat.Damage-Reduction.Damage-Multiplier", 0.8);

        // Golden Armor
        this.effectDurationMultiplier = config.getDouble("Races.Armat.Alchemy.Effect-Duration-Multiplier", 1.25);

        // Diamond Armor
        this.absoluteDamageAmount = config.getDouble("Races.Armat.Absolute-Damage.Amount", 1.0);
        this.absoluteDamageCooldown = (float) config.getDouble("Races.Armat.Absolute-Damage.Min-Attack-Cooldown", 1.0);
    }
}