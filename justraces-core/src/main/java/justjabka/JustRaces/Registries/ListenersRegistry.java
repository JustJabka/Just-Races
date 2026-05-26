package justjabka.JustRaces.Registries;

import justjabka.JustRaces.JustRacesAPI;
import justjabka.JustRaces.Listeners.GlobalListener;
import justjabka.JustRaces.Listeners.Race.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class ListenersRegistry {
    public static void register(Plugin plugin) {
        PluginManager manager = plugin.getServer().getPluginManager();

        manager.registerEvents(new GlobalListener(), plugin);
        registerRaceListeners(plugin, manager);

        JustRacesAPI.getLogger().info("Successfully registered listeners!");
    }

    private static void registerRaceListeners(Plugin plugin, PluginManager manager) {
        manager.registerEvents(new ArmatRaceListener(), plugin);
        manager.registerEvents(new PhantomRaceListener(), plugin);
        manager.registerEvents(new SkyzernRaceListener(), plugin);
        manager.registerEvents(new EpiphyteRaceListener(), plugin);
        manager.registerEvents(new LizardRaceListener(), plugin);
        manager.registerEvents(new FetrRaceListener(), plugin);

        JustRacesAPI.getLogger().info("Successfully registered race listeners!");
    }
}