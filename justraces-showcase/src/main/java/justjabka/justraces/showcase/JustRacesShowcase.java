package justjabka.justraces.showcase;

import justjabka.justraces.api.managers.ResourceManager;
import justjabka.justraces.showcase.registries.AbilitiesRegistry;
import justjabka.justraces.showcase.registries.DatapackRegistry;
import justjabka.justraces.showcase.registries.ItemModifiersRegistry;
import justjabka.justraces.showcase.registries.TraitsRegistry;
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
        ItemModifiersRegistry.register();
        TraitsRegistry.register();

        ResourceManager.registerRacesFromPlugin(this);
    }

    @Override
    public void onLoad() {
        INSTANCE = this;
        DatapackRegistry.register(this);
    }
}
