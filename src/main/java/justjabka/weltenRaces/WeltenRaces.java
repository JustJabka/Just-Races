package justjabka.weltenRaces;

import justjabka.weltenRaces.Listeners.ArmorListener;
import justjabka.weltenRaces.Races.Armat.ArmatConfig;
import justjabka.weltenRaces.Races.Armat.ArmatRaceListener;
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
        saveDefaultConfig();

        ArmatConfig armatSettings = new ArmatConfig(getConfig());

        // Event listeners
        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new ArmorListener(), this);
        pluginManager.registerEvents(new ArmatRaceListener(armatSettings), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}