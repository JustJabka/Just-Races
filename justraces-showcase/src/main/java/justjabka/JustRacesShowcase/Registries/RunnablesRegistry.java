package justjabka.JustRacesShowcase.Registries;

import justjabka.JustRacesShowcase.JustRacesShowcase;
import justjabka.JustRacesShowcase.Runnables.Race.ArmatRaceRunnable;
import org.bukkit.plugin.Plugin;

public class RunnablesRegistry {
    public static void register(Plugin plugin) {
        registerRaceRunnables(plugin);

        JustRacesShowcase.LOGGER.info("Successfully registered runnables!");
    }

    private static void registerRaceRunnables(Plugin plugin) {
        new ArmatRaceRunnable().runTaskTimer(plugin, 0L, 5L);
    }
}
