package justjabka.JustRaces.Interfaces.Configurable.Generic;

import justjabka.JustRaces.JustRacesAPI;
import justjabka.JustRaces.Managers.ResourceManager;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.ConfigurationNode;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

public interface PluginConfigurable extends Configurable {
    ConcurrentHashMap<NamespacedKey, ConfigurationNode> cachedConfig = new ConcurrentHashMap<>();

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
            ConfigurationNode node = getConfigNodeFromDisk();
            cachedConfig.put(getKey(), node);
        } catch (Exception e) {
            JustRacesAPI.getLogger().error("Failed to initialize config for '{}' in {}", getKey(), getCategory().toString(), e);
        }
    }

    @Override
    default ConfigurationNode getConfigNode() {
        return cachedConfig.computeIfAbsent(getKey(), key -> {
            try {
                return getConfigNodeFromDisk();
            } catch (Exception e) {
                throw new RuntimeException("Failed to load config for " + key, e);
            }
        });
    }

    default ConfigurationNode getConfigNodeFromDisk() {
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
