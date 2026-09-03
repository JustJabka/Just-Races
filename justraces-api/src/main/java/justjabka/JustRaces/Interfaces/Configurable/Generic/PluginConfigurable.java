package justjabka.JustRaces.Interfaces.Configurable.Generic;

import justjabka.JustRaces.JustRacesAPI;
import justjabka.JustRaces.Managers.ResourceManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.ConfigurationNode;

import java.io.File;

public interface PluginConfigurable extends Configurable {
    Category getCategory();

    default String getInternalResourcePath() {
        return getCategory() + "/" + getKey().getKey() + ".json";
    }

    default File getConfigFile() {
        File baseDir = JustRacesAPI.getInstance().getDataFolder();
        String relativePath = getCategory().toString() + "/" + getKey().getNamespace() + "/" + getKey().getKey() + ".json";

        File configFile = new File(baseDir, relativePath);
        if (configFile.getParentFile() != null && !configFile.getParentFile().exists()) {
            configFile.getParentFile().mkdirs();
        }
        return configFile;
    }

    @Override
    default void reloadConfigFile() {
        try {
            getConfigNode();
        } catch (Exception e) {
            JustRacesAPI.getLogger().error("Failed to initialize config for '{}' in {}", getKey(), getCategory().toString(), e);
        }
    }

    // TODO: actually cache configs instead of getting values from disk every time
    @Override
    default ConfigurationNode getConfigNode() {
        Plugin owningPlugin = JavaPlugin.getProvidingPlugin(this.getClass());
        return ResourceManager.loadJsonNode(owningPlugin, getConfigFile(), getInternalResourcePath());
    }

    enum Category {
        ABILITIES,
        MODIFIERS,
        TRAITS;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }
}
