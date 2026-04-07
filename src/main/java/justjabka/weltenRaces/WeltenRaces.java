package justjabka.weltenRaces;

import justjabka.weltenRaces.Races.Listeners.ArmatRaceListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class WeltenRaces extends JavaPlugin {
    public static final String PLUGIN_ID = "welten_races";
    public static final Logger LOGGER = LoggerFactory.getLogger(PLUGIN_ID);

    @Override
    public void onEnable() {
        // Plugin startup logic
        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new ArmatRaceListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}