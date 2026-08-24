package justjabka.JustRaces.Registries;

import justjabka.JustRaces.JustRacesAPI;
import justjabka.JustRaces.Runnables.GlobalRunnable;
import org.bukkit.plugin.Plugin;

public class RunnablesRegistry {
    public static void register(Plugin plugin) {
        new GlobalRunnable().runTaskTimer(plugin, 0L, 1L);

        JustRacesAPI.getLogger().info("Successfully registered runnables!");
    }
}
