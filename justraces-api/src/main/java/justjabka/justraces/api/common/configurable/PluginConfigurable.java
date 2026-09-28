package justjabka.justraces.api.common.configurable;

import justjabka.justraces.api.JustRacesAPI;
import justjabka.justraces.api.managers.ResourceManager;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.ConfigurationNode;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

public interface PluginConfigurable extends Configurable {
    ConcurrentHashMap<NamespacedKey, ConfigurationNode> cachedConfig = new ConcurrentHashMap<>();

    Category category();

    default String getInternalResourcePath() {
        return category() + "/" + getKey().getKey() + ".json";
    }

    default File getConfigFile() {
        File baseDir = JustRacesAPI.getInstance().getDataFolder();
        String relativePath = category().toString() + "/" + getKey().getNamespace() + "/" + getKey().getKey() + ".json";

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
            JustRacesAPI.getLogger().error("Failed to initialize config for '{}' in {}", getKey(), category().toString(), e);
        }
    }

    @Override
    default ConfigurationNode configNode() {
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
        ITEM_MODIFIERS,
        TRAITS;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }
}
