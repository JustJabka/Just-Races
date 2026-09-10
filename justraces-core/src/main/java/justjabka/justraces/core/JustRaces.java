package justjabka.justraces.core;

import justjabka.justraces.api.JustRacesAPI;
import justjabka.justraces.api.JustRacesRegistries;
import justjabka.justraces.api.interfaces.configurable.generic.PluginConfigurable;
import justjabka.justraces.core.registries.ListenersRegistry;
import justjabka.justraces.core.registries.RacesRegistry;
import justjabka.justraces.core.registries.RunnablesRegistry;
import justjabka.justraces.api.runnables.generic.BaseTraitRunnable;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class JustRaces extends JavaPlugin {

    @Override
    public void onEnable() {
        // Add Hooks
        JustRacesRegistries.ABILITIES.addHook((_, ability) -> {
            Bukkit.getPluginManager().registerEvents(ability, JustRacesAPI.getInstance());

            if (ability instanceof PluginConfigurable configurable) {
                configurable.initializeConfigFile();
            }
        });

        JustRacesRegistries.MODIFIERS.addHook((_, modifier) -> {
            if (modifier instanceof Listener listener) {
                Bukkit.getPluginManager().registerEvents(listener, JustRacesAPI.getInstance());
            }

            if (modifier instanceof PluginConfigurable configurable) {
                configurable.initializeConfigFile();
            }
        });

        JustRacesRegistries.TRAITS.addHook((_, trait) -> {
            if (trait instanceof Listener listener) {
                Bukkit.getPluginManager().registerEvents(listener, JustRacesAPI.getInstance());
            }

            if (trait instanceof BaseTraitRunnable runnable) {
                runnable.runTaskTimer(JustRacesAPI.getInstance(), 0L, runnable.getTickPeriod());
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