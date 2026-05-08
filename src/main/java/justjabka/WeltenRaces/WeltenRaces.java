package justjabka.WeltenRaces;

import justjabka.WeltenRaces.Registries.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

public final class WeltenRaces extends JavaPlugin {
    public static WeltenRaces INSTANCE;
    public static String NAMESPACE;
    public static Logger LOGGER;

    @Override
    public void onEnable() {
        ConfigRegistry configs = new ConfigRegistry(this);

        ListenersRegistry.register(this, configs);
        RunnablesRegistry.register(this, configs);
        AbilitiesRegistry.register(this, configs);
        ModifierRegistry.register(configs);
    }

    @Override
    public void onLoad() {
        INSTANCE = this;
        NAMESPACE = this.namespace();
        LOGGER = getSLF4JLogger();

        DatapackRegistry.register(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}