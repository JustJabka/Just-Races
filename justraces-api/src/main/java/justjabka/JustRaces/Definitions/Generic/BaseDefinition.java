package justjabka.JustRaces.Definitions.Generic;

import com.google.gson.JsonElement;
import justjabka.JustRaces.JustRacesAPI;
import org.bukkit.NamespacedKey;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.Map;

public abstract class BaseDefinition {
    private transient String key;
    private Map<String, Object> config;

    private transient ConfigurationNode configNode;

    // Key
    public NamespacedKey getKey() {
        return NamespacedKey.fromString(key, JustRacesAPI.getInstance());
    }

    public void setKey(String key) {
        this.key = key;
    }

    // Config
    public ConfigurationNode getConfig() {
        if (configNode == null) {
            try {
                configNode = BasicConfigurationNode.root();

                if (config != null) {
                    configNode.set(config);
                }
            } catch (Exception e) {
                JustRacesAPI.getLogger().error("Failed to map configuration for instance: {}", key, e);
            }
        }

        return configNode;
    }

    public void clearConfigCache() {
        this.configNode = null;
    }

    protected boolean isEmptyObject(JsonElement element) {
        return element.isJsonObject() && element.getAsJsonObject().asMap().isEmpty();
    }

    protected boolean isString(JsonElement element) {
        return element.isJsonPrimitive() && element.getAsJsonPrimitive().isString();
    }
}
