package justjabka.JustRaces.Registries;

import justjabka.JustRaces.JustRaces;
import justjabka.JustRaces.Listeners.*;
import justjabka.JustRaces.Listeners.Race.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class ListenersRegistry {
    public static void register(Plugin plugin) {
        PluginManager manager = plugin.getServer().getPluginManager();

        manager.registerEvents(new GlobalListener(), plugin);
        registerRaceListeners(plugin, manager);

        JustRaces.LOGGER.info("Successfully registered listeners!");
    }

    private static void registerRaceListeners(Plugin plugin, PluginManager manager) {
        manager.registerEvents(new ArmatRaceListener(), plugin);
        manager.registerEvents(new PhantomRaceListener(), plugin);
        manager.registerEvents(new SkyzernRaceListener(), plugin);
        manager.registerEvents(new EpiphyteRaceListener(), plugin);
        manager.registerEvents(new LizardRaceListener(), plugin);
        manager.registerEvents(new FetrRaceListener(), plugin);

        JustRaces.LOGGER.info("Successfully registered race listeners!");
    }
}