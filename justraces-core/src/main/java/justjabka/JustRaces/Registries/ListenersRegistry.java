package justjabka.JustRaces.Registries;

import justjabka.JustRaces.JustRacesAPI;
import justjabka.JustRaces.Listeners.GlobalListener;
import justjabka.JustRaces.Listeners.TriggerListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class ListenersRegistry {
    public static void register(Plugin plugin) {
        PluginManager manager = plugin.getServer().getPluginManager();

        manager.registerEvents(new GlobalListener(), plugin);
        manager.registerEvents(new TriggerListener(), plugin);

        JustRacesAPI.getLogger().info("Successfully registered listeners!");
    }
}