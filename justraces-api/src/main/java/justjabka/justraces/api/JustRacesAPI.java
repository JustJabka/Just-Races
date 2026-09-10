package justjabka.justraces.api;

import org.bukkit.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JustRacesAPI {
    private static Plugin INSTANCE;
    public static final String NAMESPACE = "justraces";
    private static Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);

    public static Plugin getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("JustRaces API is not initialized!");
        }
        return INSTANCE;
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public static void init(Plugin plugin, Logger pluginLogger) {
        INSTANCE = plugin;
        LOGGER = pluginLogger;
    }
}
