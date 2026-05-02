package justjabka.WeltenRaces.Configs.Race;

import org.bukkit.configuration.file.FileConfiguration;

public class PhantomConfig {
    public final double membraneHealAmount;
    public final int meatBonusFoodAmount;
    public final int meatBonusRegenerationDuration;
    public final int insomniaDamageBonus;

    public PhantomConfig(FileConfiguration config) {
        this.membraneHealAmount = config.getDouble("membrane-heal-amount", 1);
        this.meatBonusFoodAmount = config.getInt("meat-bonus.food-amount", 5);
        this.meatBonusRegenerationDuration = config.getInt("meat-bonus.regeneration-duration", 2) * 20;
        this.insomniaDamageBonus = config.getInt("insomnia-damage-bonus", 3);
    }
}