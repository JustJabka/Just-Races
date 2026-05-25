package justjabka.JustRaces.Registries;

import io.papermc.paper.datapack.Datapack;
import io.papermc.paper.datapack.DatapackManager;
import justjabka.JustRaces.JustRaces;
import org.bukkit.plugin.Plugin;

public class DatapackRegistry {
    public static void register(Plugin plugin) {
        DatapackManager datapackManager = plugin.getServer().getDatapackManager();
        String pluginName = plugin.getPluginMeta().getName();

        Datapack pack = datapackManager.getPack(pluginName + "/provided");

        if (pack == null) return;

        if (pack.isEnabled()) {
            JustRaces.LOGGER.info("The datapack loaded successfully!");
        } else {
            JustRaces.LOGGER.warn("The datapack failed to load :(");
        }
    }
}
