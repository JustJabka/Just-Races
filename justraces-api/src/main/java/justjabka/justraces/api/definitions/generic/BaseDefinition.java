package justjabka.justraces.api.definitions.generic;

import justjabka.justraces.api.JustRacesAPI;
import org.bukkit.NamespacedKey;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.Map;

@SuppressWarnings({"unused", "MismatchedQueryAndUpdateOfCollection"})
public abstract class BaseDefinition {
    private Map<String, Object> config;

    private transient String key;
    private transient ConfigurationNode cachedConfig;

    // region Key
    public NamespacedKey getKey() {
        return NamespacedKey.fromString(key, JustRacesAPI.getInstance());
    }

    public void setKey(String key) {
        this.key = key;
    }
    // endregion

    // region Config
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
    // endregion
}
