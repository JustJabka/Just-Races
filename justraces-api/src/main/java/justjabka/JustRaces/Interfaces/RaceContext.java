package justjabka.JustRaces.Interfaces;

import justjabka.JustRaces.Managers.RaceManager;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Arrays;
import java.util.List;

public interface RaceContext {
    NamespacedKey getRaceKey();

    private CommentedConfigurationNode getRawConfigNode(Object... path) {
        CommentedConfigurationNode root = RaceManager.getRaceByKey(getRaceKey()).getConfig();
        if (path.length == 0) return root;

        CommentedConfigurationNode node = root.node(path);
        if (node.virtual()) {
            throw new IllegalStateException(
                    "Missing required config key for race '%s': %s".formatted(
                            getRaceKey(),
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

    default boolean isRequiredRace(Player player) {
        return RaceManager.isRace(player, getRaceKey());
    }
}
