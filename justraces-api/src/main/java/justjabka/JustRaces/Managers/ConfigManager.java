package justjabka.JustRaces.Managers;

import justjabka.JustRaces.Configs.ConfigWrapper;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigManager {
    public static FileConfiguration loadAbilityConfig(Plugin plugin, String name) {
        return new ConfigWrapper(plugin, "configs/abilities/%s.yml".formatted(name)).getConfig();
    }

    public static FileConfiguration loadModifierConfig(Plugin plugin, String name) {
        return new ConfigWrapper(plugin, "configs/modifiers/%s.yml".formatted(name)).getConfig();
    }
}
