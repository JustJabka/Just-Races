package justjabka.JustRaces;

import justjabka.JustRaces.Registries.*;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

public final class JustRaces extends JavaPlugin {
    public static JustRaces INSTANCE;
    public static final String NAMESPACE = "justraces";
    public static Logger LOGGER;

    @Override
    public void onEnable() {
        ConfigRegistry configs = new ConfigRegistry(this);

        RacesRegistry.register(this);
        ListenersRegistry.register(this);
        RunnablesRegistry.register(this);
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