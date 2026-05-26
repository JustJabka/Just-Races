package justjabka.JustRaces;

import justjabka.JustRaces.Managers.ConfigManager;
import justjabka.JustRaces.Registries.*;
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
            ConfigManager.loadAbilityConfigNew(ability, JustRacesAPI.getInstance());
        });

        JustRacesRegistries.MODIFIERS.addHook((key, modifier) -> {
            if (modifier instanceof Listener listener) {
                Bukkit.getPluginManager().registerEvents(listener, JustRacesAPI.getInstance());
            }
        });

        // Register
        ConfigRegistry configs = new ConfigRegistry(this);

        RacesRegistry.register(this);
        AbilitiesRegistry.register(configs);
        ModifiersRegistry.register(configs);

        ListenersRegistry.register(this);
        RunnablesRegistry.register(this);
    }

    @Override
    public void onLoad() {
        JustRacesAPI.init(this, getSLF4JLogger());

        DatapackRegistry.register(this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }
}