package justjabka.justraces.showcase.registries;

import io.papermc.paper.datapack.Datapack;
import io.papermc.paper.datapack.DatapackManager;
import justjabka.justraces.showcase.JustRacesShowcase;
import org.bukkit.plugin.Plugin;

public class DatapackRegistry {
    public static void register(Plugin plugin) {
        DatapackManager datapackManager = plugin.getServer().getDatapackManager();
        String pluginName = plugin.getPluginMeta().getName();

        Datapack pack = datapackManager.getPack(pluginName + "/provided");

        if (pack == null) return;

        if (pack.isEnabled()) {
            JustRacesShowcase.LOGGER.info("The datapack loaded successfully!");
        } else {
            JustRacesShowcase.LOGGER.warn("The datapack failed to load :(");
        }
    }
}
