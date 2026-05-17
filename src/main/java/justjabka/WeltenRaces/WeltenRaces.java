package justjabka.WeltenRaces;

import justjabka.WeltenRaces.Registries.*;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

public final class WeltenRaces extends JavaPlugin {
    public static WeltenRaces INSTANCE;
    public static final String NAMESPACE = "weltenraces";
    public static Logger LOGGER;

    @Override
    public void onEnable() {
        ConfigRegistry configs = new ConfigRegistry(this);

        RacesRegistry.register(this);
        ListenersRegistry.register(this, configs);
        RunnablesRegistry.register(this, configs);
        AbilitiesRegistry.register(this, configs);
        ModifiersRegistry.register(this, configs);
    }

    @Override
    public void onLoad() {
        INSTANCE = this;
        LOGGER = getSLF4JLogger();

        DatapackRegistry.register(this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }
}