package justjabka.justraces.showcase.registries;

import justjabka.justraces.showcase.JustRacesShowcase;
import justjabka.justraces.showcase.runnables.race.ArmatRaceRunnable;
import justjabka.justraces.showcase.runnables.race.BuzzlingRaceRunnable;
import org.bukkit.plugin.Plugin;

public class RunnablesRegistry {
    public static void register(Plugin plugin) {
        registerRaceRunnables(plugin);

        JustRacesShowcase.LOGGER.info("Successfully registered runnables!");
    }

    private static void registerRaceRunnables(Plugin plugin) {
        new ArmatRaceRunnable().runTaskTimer(plugin, 0L, 5L);
        new BuzzlingRaceRunnable().runTaskTimer(plugin, 0L, 200L);
    }
}
