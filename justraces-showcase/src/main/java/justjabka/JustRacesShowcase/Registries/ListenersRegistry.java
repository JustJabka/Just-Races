package justjabka.JustRacesShowcase.Registries;

import justjabka.JustRacesShowcase.JustRacesShowcase;
import justjabka.JustRacesShowcase.Listeners.Race.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class ListenersRegistry {
    public static void register(Plugin plugin) {
        registerRaceListeners(plugin);

        JustRacesShowcase.LOGGER.info("Successfully registered listeners!");
    }

    private static void registerRaceListeners(Plugin plugin) {
        PluginManager manager = plugin.getServer().getPluginManager();

        manager.registerEvents(new ArmatRaceListener(), plugin);

        JustRacesShowcase.LOGGER.info("Successfully registered race listeners!");
    }
}
