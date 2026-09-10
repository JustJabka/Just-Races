package justjabka.justraces.api.interfaces.configurable.generic;

import org.bukkit.NamespacedKey;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Arrays;
import java.util.List;

public interface Configurable {
    NamespacedKey getKey();
    ConfigurationNode getConfigNode();
    void reloadConfigFile();

    default void initializeConfigFile() {
        reloadConfigFile();
    }

    private ConfigurationNode getRawConfigNode(Object... path) {
        ConfigurationNode root = getConfigNode();
        if (path.length == 0) return root;

        ConfigurationNode node = root.node(path);
        if (node.virtual()) {
            throw new IllegalStateException(
                    "Missing required config key for '%s': %s".formatted(
                            getKey(),
                            Arrays.toString(path)
                    ));
        }
        return node;
    }

    default double getConfigDouble(Object... path) {
        return getRawConfigNode(path).getDouble();
    }

    default float getConfigFloat(Object... path) {
        return getRawConfigNode(path).getFloat();
    }

    default int getConfigInt(Object... path) {
        return getRawConfigNode(path).getInt();
    }

    default long getConfigLong(Object... path) {
        return getRawConfigNode(path).getLong();
    }

    default boolean getConfigBoolean(Object... path) {
        return getRawConfigNode(path).getBoolean();
    }

    default String getConfigString(Object... path) {
        return getRawConfigNode(path).getString();
    }

    default <V> List<V> getConfigList(Class<V> type, Object... path) {
        try {
            return getRawConfigNode(path).getList(type);
        } catch (SerializationException e) {
            throw new RuntimeException("Failed to deserialize config list", e);
        }
    }

    default <T> T getConfigValue(Class<T> type, Object... path) {
        try {
            T val = getRawConfigNode(path).get(type);
            if (val == null) {
                throw new IllegalStateException(
                        "Config value for type %s is null at path: %s".formatted(
                                type.getSimpleName(),
                                Arrays.toString(path)
                        ));
            }
            return val;
        } catch (SerializationException e) {
            throw new RuntimeException("Failed to deserialize config at %s".formatted(Arrays.toString(path)), e);
        }
    }
}
