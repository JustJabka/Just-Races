package justjabka.WeltenRaces;

import io.papermc.paper.datapack.Datapack;
import justjabka.WeltenRaces.Configs.Race.ArmatConfig;
import justjabka.WeltenRaces.RaceListeners.ArmatRaceListener;
import justjabka.WeltenRaces.RaceListeners.BaseRaceListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class WeltenRaces extends JavaPlugin {
    public static WeltenRaces INSTANCE;
    public static final String PLUGIN_ID = "welten_races";
    public static final Logger LOGGER = LoggerFactory.getLogger(PLUGIN_ID);

    @Override
    public void onEnable() {
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
    public void onLoad() {
        INSTANCE = this;

        // Load Datapack
        Datapack pack = this.getServer().getDatapackManager().getPack(getPluginMeta().getName() + "/provided");

        if (pack == null) return;

        if (pack.isEnabled()) {
            LOGGER.info("The datapack loaded successfully!");
        } else {
            LOGGER.warn("The datapack failed to load :(");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}