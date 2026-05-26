package justjabka.JustRaces.Managers;

import justjabka.JustRaces.Abilities.Generic.BaseAbility;
import justjabka.JustRaces.Configs.ConfigWrapper;
import justjabka.JustRaces.JustRacesAPI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;

import java.io.File;

public class ConfigManager {
    public static void loadAbilityConfigNew(BaseAbility ability, Plugin plugin) {
        String fileName = ability.getKey().getKey() + ".json";
        File configFile = new File(plugin.getDataFolder(), "abilities/" + fileName);

        if (!configFile.getParentFile().exists()) {
            configFile.getParentFile().mkdirs();
        }

        GsonConfigurationLoader loader = GsonConfigurationLoader.builder()
                .file(configFile)
                .defaultOptions(options -> options.shouldCopyDefaults(true))
                .build();

        try {
            BasicConfigurationNode rootNode = loader.load();

            rootNode.get(ability.getClass(), ability);

            loader.save(rootNode);
        } catch (Exception e) {
            JustRacesAPI.getLogger().error("Failed to load ability config for {}", ability.getKey(), e);
        }
    }

    public static FileConfiguration loadAbilityConfig(Plugin plugin, String name) {
        return new ConfigWrapper(plugin, "configs/abilities/%s.yml".formatted(name)).getConfig();
    }

    public static FileConfiguration loadModifierConfig(Plugin plugin, String name) {
        return new ConfigWrapper(plugin, "configs/modifiers/%s.yml".formatted(name)).getConfig();
    }
}
