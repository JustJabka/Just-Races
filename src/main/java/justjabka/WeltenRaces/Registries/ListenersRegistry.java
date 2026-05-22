package justjabka.WeltenRaces.Registries;

import justjabka.WeltenRaces.Listeners.*;
import justjabka.WeltenRaces.Listeners.Race.*;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class ListenersRegistry {
    public static void register(Plugin plugin) {
        PluginManager manager = plugin.getServer().getPluginManager();

        manager.registerEvents(new GlobalListener(), plugin);
        registerRaceListeners(plugin, manager);

        WeltenRaces.LOGGER.info("Successfully registered listeners!");
    }

    private static void registerRaceListeners(Plugin plugin, PluginManager manager) {
        manager.registerEvents(new ArmatRaceListener(), plugin);
        manager.registerEvents(new PhantomRaceListener(), plugin);
        manager.registerEvents(new SkyzernRaceListener(), plugin);
        manager.registerEvents(new EpiphyteRaceListener(), plugin);
        manager.registerEvents(new LizardRaceListener(), plugin);
        manager.registerEvents(new FetrRaceListener(), plugin);

        WeltenRaces.LOGGER.info("Successfully registered race listeners!");
    }
}