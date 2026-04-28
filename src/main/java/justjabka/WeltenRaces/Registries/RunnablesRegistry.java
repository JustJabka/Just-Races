package justjabka.WeltenRaces.Registries;

import justjabka.WeltenRaces.Runnables.PhantomRaceRunnable;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.plugin.Plugin;

public class RunnablesRegistry {
    public static void register(Plugin plugin) {
        new PhantomRaceRunnable().runTaskTimer(plugin, 0L, 20L);

        WeltenRaces.LOGGER.info("Successfully registered runnables!");
    }
}
