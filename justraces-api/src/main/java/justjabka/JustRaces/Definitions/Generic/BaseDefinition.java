package justjabka.JustRaces.Definitions.Generic;

import justjabka.JustRaces.JustRacesAPI;
import org.bukkit.NamespacedKey;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.Map;

@SuppressWarnings({"unused", "MismatchedQueryAndUpdateOfCollection"})
public abstract class BaseDefinition {
    private transient String key;
    private Map<String, Object> config;

    private transient ConfigurationNode cachedConfig;

    // Key
    public NamespacedKey getKey() {
        return NamespacedKey.fromString(key, JustRacesAPI.getInstance());
    }

    public void setKey(String key) {
        this.key = key;
    }

    // Config
    public ConfigurationNode getConfig() {
        if (cachedConfig == null) buildConfigCache();
        return cachedConfig;
    }

    public void clearConfigCache() {
        this.cachedConfig = null;
    }

    public abstract void clearDefinitionCache();

    private void buildConfigCache() {
        try {
            cachedConfig = BasicConfigurationNode.root();

            if (config != null) {
                cachedConfig.set(config);
            }
        } catch (Exception e) {
            JustRacesAPI.getLogger().error("Failed to map configuration for instance: {}", key, e);
        }
    }
}
