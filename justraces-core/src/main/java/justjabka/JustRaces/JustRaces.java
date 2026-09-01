package justjabka.JustRaces;

import justjabka.JustRaces.Interfaces.Configurable.Generic.PluginConfigurable;
import justjabka.JustRaces.Registries.ListenersRegistry;
import justjabka.JustRaces.Registries.RacesRegistry;
import justjabka.JustRaces.Registries.RunnablesRegistry;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class JustRaces extends JavaPlugin {

    @Override
    public void onEnable() {
        // Add Hooks
        JustRacesRegistries.ABILITIES.addHook((key, ability) -> {
            Bukkit.getPluginManager().registerEvents(ability, JustRacesAPI.getInstance());

            if (ability instanceof PluginConfigurable configurable) {
                configurable.initializeConfigFile();
            }
        });

        JustRacesRegistries.MODIFIERS.addHook((key, modifier) -> {
            if (modifier instanceof Listener listener) {
                Bukkit.getPluginManager().registerEvents(listener, JustRacesAPI.getInstance());
            }

            if (modifier instanceof PluginConfigurable configurable) {
                configurable.initializeConfigFile();
            }
        });

        // Register
        Bukkit.getScheduler().runTask(this, () -> {
            RacesRegistry.loadAllRaces();

            ListenersRegistry.register(this);
            RunnablesRegistry.register(this);
        });
    }

    @Override
    public void onLoad() {
        JustRacesAPI.init(this, getSLF4JLogger());
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }
}