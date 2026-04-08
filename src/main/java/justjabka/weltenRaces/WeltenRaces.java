package justjabka.weltenRaces;

import justjabka.weltenRaces.Races.Armat.ArmatConfig;
import justjabka.weltenRaces.Races.Armat.ArmatRaceListener;
import justjabka.weltenRaces.Races.Generic.BaseRaceListener;
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

        // Configs
        ArmatConfig armatSettings = new ArmatConfig(getConfig());

        // Global Listeners
        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new BaseRaceListener(), this);

        // Race Specific Listeners
        pluginManager.registerEvents(new ArmatRaceListener(armatSettings), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}