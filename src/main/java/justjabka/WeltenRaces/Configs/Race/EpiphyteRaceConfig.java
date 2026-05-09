package justjabka.WeltenRaces.Configs.Race;

import org.bukkit.configuration.file.FileConfiguration;

public class EpiphyteRaceConfig {
    public final double mossMovementSpeedBonus;

    public EpiphyteRaceConfig(FileConfiguration config) {
        this.mossMovementSpeedBonus = config.getDouble("moss-movement-speed-bonus", 0.1);
    }
}
