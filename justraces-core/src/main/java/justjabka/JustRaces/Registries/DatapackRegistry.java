package justjabka.JustRaces.Registries;

import io.papermc.paper.datapack.Datapack;
import io.papermc.paper.datapack.DatapackManager;
import justjabka.JustRaces.JustRacesAPI;
import org.bukkit.plugin.Plugin;

public class DatapackRegistry {
    public static void register(Plugin plugin) {
        DatapackManager datapackManager = plugin.getServer().getDatapackManager();
        String pluginName = plugin.getPluginMeta().getName();

        Datapack pack = datapackManager.getPack(pluginName + "/provided");

        if (pack == null) return;

        if (pack.isEnabled()) {
            JustRacesAPI.getLogger().info("The datapack loaded successfully!");
        } else {
            JustRacesAPI.getLogger().warn("The datapack failed to load :(");
        }
    }
}
