package justjabka.WeltenRaces.Configs.Race;

import org.bukkit.configuration.file.FileConfiguration;

public class FetrRaceConfig {
    public final double statusUpdateRadius;
    public final int hornBuffDuration;
    public final double hornBuffRadius;
    public final int hornBuffNotesPerPlayer;

    public FetrRaceConfig(FileConfiguration config) {
        this.statusUpdateRadius = config.getDouble("status-update-radius", 50);
        this.hornBuffDuration = config.getInt("horn-buff.duration", 7) * 20;
        this.hornBuffRadius = config.getDouble("horn-buff.radius", 15);
        this.hornBuffNotesPerPlayer = config.getInt("horn-buff.notes-per-player", 2);
    }
}
