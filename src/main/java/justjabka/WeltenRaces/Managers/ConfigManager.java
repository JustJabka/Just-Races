package justjabka.WeltenRaces.Managers;

import justjabka.WeltenRaces.Configs.ConfigWrapper;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigManager {
    public static FileConfiguration loadAbilityConfig(Plugin plugin, String name) {
        return new ConfigWrapper(plugin, "configs/abilities/%s.yml".formatted(name)).getConfig();
    }

    public static FileConfiguration loadRaceConfig(Plugin plugin, String name) {
        return new ConfigWrapper(plugin, "configs/races/%s.yml".formatted(name)).getConfig();
    }

    public static FileConfiguration loadModifierConfig(Plugin plugin, String name) {
        return new ConfigWrapper(plugin, "configs/modifiers/%s.yml".formatted(name)).getConfig();
    }
}
