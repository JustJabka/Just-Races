package justjabka.JustRacesShowcase.Registries;

import justjabka.JustRacesShowcase.JustRacesShowcase;
import justjabka.JustRacesShowcase.Listeners.Race.ArmatRaceListener;
import justjabka.JustRacesShowcase.Listeners.Race.BeeRaceListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class ListenersRegistry {
    public static void register(Plugin plugin) {
        PluginManager manager = plugin.getServer().getPluginManager();

        registerRaceListeners(plugin, manager);

        JustRacesShowcase.LOGGER.info("Successfully registered listeners!");
    }

    private static void registerRaceListeners(Plugin plugin, PluginManager manager) {
        manager.registerEvents(new ArmatRaceListener(), plugin);
        manager.registerEvents(new BeeRaceListener(), plugin);

        JustRacesShowcase.LOGGER.info("Successfully registered race listeners!");
    }
}
