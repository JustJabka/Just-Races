package justjabka.WeltenRaces.Instances.Generic;

import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.NamespacedKey;
import org.spongepowered.configurate.CommentedConfigurationNode;

import java.util.Map;

public abstract class BaseInstance {
    private transient String key;
    private Map<String, Object> config;
    private transient CommentedConfigurationNode configNode;

    // Key
    public NamespacedKey getKey() {
        return NamespacedKey.fromString(key, WeltenRaces.INSTANCE);
    }

    public void setKey(String key) {
        this.key = key;
    }

    // Config
    public CommentedConfigurationNode getConfig() {
        if (configNode == null) {
            try {
                configNode = CommentedConfigurationNode.root();

                if (config != null) {
                    configNode.set(config);
                }
            } catch (Exception e) {
                WeltenRaces.LOGGER.error("Failed to map configuration for instance: {}", key, e);
            }
        }

        return configNode;
    }

    public void clearConfigCache() {
        this.configNode = null;
    }
}
