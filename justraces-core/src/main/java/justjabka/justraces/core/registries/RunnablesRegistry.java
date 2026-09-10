package justjabka.justraces.core.registries;

import justjabka.justraces.api.JustRacesAPI;
import justjabka.justraces.core.runnables.GlobalRunnable;
import org.bukkit.plugin.Plugin;

public class RunnablesRegistry {
    public static void register(Plugin plugin) {
        new GlobalRunnable().runTaskTimer(plugin, 0L, 1L);

        JustRacesAPI.getLogger().info("Successfully registered runnables!");
    }
}
