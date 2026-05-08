package justjabka.WeltenRaces.Registries;

import justjabka.WeltenRaces.Listeners.*;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class ListenersRegistry {
    public static void register(Plugin plugin, ConfigRegistry configs) {
        PluginManager manager = plugin.getServer().getPluginManager();

        manager.registerEvents(new BaseRaceListener(), plugin);
        registerRaceListeners(plugin, manager, configs);

        WeltenRaces.LOGGER.info("Successfully registered listeners!");
    }

    private static void registerRaceListeners(Plugin plugin, PluginManager manager, ConfigRegistry configs) {
        manager.registerEvents(new ArmatRaceListener(configs.armatRaceConfig), plugin);
        manager.registerEvents(new PhantomRaceListener(configs.phantomRaceConfig), plugin);
        manager.registerEvents(new SkyzernRaceListener(configs.skyzernRaceConfig), plugin);
        manager.registerEvents(new EpiphyteRaceListener(), plugin);

        WeltenRaces.LOGGER.info("Successfully registered race listeners!");
    }
}