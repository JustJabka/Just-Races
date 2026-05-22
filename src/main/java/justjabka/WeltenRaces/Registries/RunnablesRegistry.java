package justjabka.WeltenRaces.Registries;

import justjabka.WeltenRaces.Runnables.GlobalRunnable;
import justjabka.WeltenRaces.Runnables.Race.*;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.plugin.Plugin;

public class RunnablesRegistry {
    public static void register(Plugin plugin) {
        new GlobalRunnable().runTaskTimer(plugin, 0L, 10L);

        registerRaceRunnables(plugin);

        WeltenRaces.LOGGER.info("Successfully registered runnables!");
    }

    private static void registerRaceRunnables(Plugin plugin) {
        new ArmatRaceRunnable().runTaskTimer(plugin, 0L, 5L);
        new PhantomRaceRunnable().runTaskTimer(plugin, 0L, 20L);
        new SkyzernRaceRunnable().runTaskTimer(plugin, 0L, 20L);
        new EpiphyteRaceRunnable().runTaskTimer(plugin, 0L, 20L);
        new LizardRaceRunnable().runTaskTimer(plugin, 0L, 20L);
        new FetrRaceRunnable().runTaskTimer(plugin, 0L, 20L);
    }
}
