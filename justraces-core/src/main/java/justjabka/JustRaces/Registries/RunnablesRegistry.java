package justjabka.JustRaces.Registries;

import justjabka.JustRaces.JustRacesAPI;
import justjabka.JustRaces.Runnables.GlobalRunnable;
import justjabka.JustRaces.Runnables.Race.*;
import org.bukkit.plugin.Plugin;

public class RunnablesRegistry {
    public static void register(Plugin plugin) {
        new GlobalRunnable().runTaskTimer(plugin, 0L, 10L);

        registerRaceRunnables(plugin);

        JustRacesAPI.getLogger().info("Successfully registered runnables!");
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
