package justjabka.JustRaces.Registries;

import justjabka.JustRaces.JustRacesAPI;
import justjabka.JustRaces.Listeners.GlobalListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class ListenersRegistry {
    public static void register(Plugin plugin) {
        PluginManager manager = plugin.getServer().getPluginManager();

        manager.registerEvents(new GlobalListener(), plugin);

        JustRacesAPI.getLogger().info("Successfully registered listeners!");
    }
}