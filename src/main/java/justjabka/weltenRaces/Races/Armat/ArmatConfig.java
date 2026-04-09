package justjabka.weltenRaces.Races.Armat;

import org.bukkit.configuration.file.FileConfiguration;

public class ArmatConfig {
    public final double vulnerableMultiplier;
    public final double sinkGravity;
    public final double inversionMin;
    public final double inversionMax;
    public final double reductionStart;
    public final double reductionMultiplier;

    public ArmatConfig(FileConfiguration config) {
        this.vulnerableMultiplier = config.getDouble("Races.Armat.Vulnerable-Damage-Multiplier", 1.5);
        this.sinkGravity = config.getDouble("Races.Armat.Sink-Gravity-Value", 0.32);
        this.inversionMin = config.getDouble("Races.Armat.Damage-Inversion.Lower-Bound", 3.0);
        this.inversionMax = config.getDouble("Races.Armat.Damage-Inversion.Upper-Bound", 30.0);
        this.reductionStart = config.getDouble("Races.Armat.Damage-Reduction.Starting-Point", 8.0);
        this.reductionMultiplier = config.getDouble("Races.Armat.Damage-Reduction.Damage-Multiplier", 0.8);
    }
}