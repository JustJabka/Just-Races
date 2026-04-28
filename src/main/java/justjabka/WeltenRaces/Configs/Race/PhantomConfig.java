package justjabka.WeltenRaces.Configs.Race;

import org.bukkit.configuration.file.FileConfiguration;

public class PhantomConfig {
    public final double membraneHealAmount;

    public PhantomConfig(FileConfiguration config) {
        this.membraneHealAmount = config.getDouble("membrane-heal-amount", 1);
    }
}