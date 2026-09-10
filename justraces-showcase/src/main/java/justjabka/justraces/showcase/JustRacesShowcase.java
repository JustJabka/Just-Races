package justjabka.justraces.showcase;

import justjabka.justraces.api.managers.ResourceManager;
import justjabka.justraces.showcase.registries.*;
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
        AbilitiesRegistry.register();
        ModifiersRegistry.register();
        TraitsRegistry.register();

        ListenersRegistry.register(this);
        RunnablesRegistry.register(this);

        ResourceManager.registerRacesFromPlugin(this);
    }

    @Override
    public void onLoad() {
        INSTANCE = this;
        DatapackRegistry.register(this);
    }
}
