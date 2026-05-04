package justjabka.WeltenRaces.Registries;

import justjabka.WeltenRaces.Configs.ConfigWrapper;
import justjabka.WeltenRaces.Configs.Race.ArmatConfig;
import justjabka.WeltenRaces.Configs.Race.PhantomConfig;
import justjabka.WeltenRaces.Listeners.ArmatRaceListener;
import justjabka.WeltenRaces.Listeners.BaseRaceListener;
import justjabka.WeltenRaces.Listeners.PhantomRaceListener;
import justjabka.WeltenRaces.Listeners.SkyzernRaceListener;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class ListenersRegistry {
    public static void register(Plugin plugin) {
        PluginManager pluginManager = plugin.getServer().getPluginManager();

        pluginManager.registerEvents(new BaseRaceListener(), plugin);
        registerRaceListeners(plugin, pluginManager);

        WeltenRaces.LOGGER.info("Successfully registered listeners!");
    }

    private static void registerRaceListeners(Plugin plugin, PluginManager pluginManager) {
        // Get Configs
        ArmatConfig armatConfig = new ArmatConfig(loadRaceConfig(plugin, "armat"));
        PhantomConfig phantomConfig = new PhantomConfig(loadRaceConfig(plugin, "phantom"));

        // Register
        pluginManager.registerEvents(new ArmatRaceListener(armatConfig), plugin);
        pluginManager.registerEvents(new PhantomRaceListener(phantomConfig), plugin);
        pluginManager.registerEvents(new SkyzernRaceListener(), plugin);

        WeltenRaces.LOGGER.info("Successfully registered race listeners!");
    }

    private static FileConfiguration loadRaceConfig(Plugin plugin, String name) {
        return new ConfigWrapper(plugin, "races/%s.yml".formatted(name)).getConfig();
    }
}