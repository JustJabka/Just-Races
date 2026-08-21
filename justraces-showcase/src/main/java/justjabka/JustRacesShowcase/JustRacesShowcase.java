package justjabka.JustRacesShowcase;

import justjabka.JustRacesShowcase.Registries.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JustRacesShowcase extends JavaPlugin {
    public static Plugin INSTANCE;
    public static final String NAMESPACE = "justracesshowcase";
    public static Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);

    @Override
    public void onEnable() {
        ConfigRegistry configs = new ConfigRegistry(this);

        AbilitiesRegistry.register(configs);
        ModifiersRegistry.register(configs);

        ListenersRegistry.register(this);
        RunnablesRegistry.register(this);
    }

    @Override
    public void onLoad() {
        INSTANCE = this;
        DatapackRegistry.register(this);
    }
}
