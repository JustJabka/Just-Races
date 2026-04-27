package justjabka.WeltenRaces;

import io.papermc.paper.datapack.Datapack;
import justjabka.WeltenRaces.Configs.Race.ArmatConfig;
import justjabka.WeltenRaces.Configs.Race.PhantomConfig;
import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.RaceListeners.ArmatRaceListener;
import justjabka.WeltenRaces.RaceListeners.BaseRaceListener;
import justjabka.WeltenRaces.RaceListeners.PhantomRaceListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

public final class WeltenRaces extends JavaPlugin {
    public static WeltenRaces INSTANCE;
    public static String NAMESPACE;
    public static Logger LOGGER;

    @Override
    public void onEnable() {
        // Configs
        saveDefaultConfig();

        ArmatConfig armatSettings = new ArmatConfig(getConfig());
        PhantomConfig phantomSettings = new PhantomConfig(getConfig());

        // Global Listeners
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new BaseRaceListener(), this);

        AbilityManager abilityManager = new AbilityManager();
        abilityManager.loadAbilityListeners();

        // Race Specific Listeners
        pluginManager.registerEvents(new ArmatRaceListener(armatSettings), this);
        pluginManager.registerEvents(new PhantomRaceListener(phantomSettings), this);
    }

    @Override
    public void onLoad() {
        INSTANCE = this;
        NAMESPACE = this.namespace();
        LOGGER = getSLF4JLogger();

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