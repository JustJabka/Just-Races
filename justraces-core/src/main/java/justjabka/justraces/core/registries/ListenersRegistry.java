package justjabka.justraces.core.registries;

import justjabka.justraces.api.JustRacesAPI;
import justjabka.justraces.core.listeners.GlobalListener;
import justjabka.justraces.core.listeners.TriggerListener;
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