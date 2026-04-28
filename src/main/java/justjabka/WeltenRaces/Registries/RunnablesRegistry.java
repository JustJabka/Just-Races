package justjabka.WeltenRaces.Registries;

import justjabka.WeltenRaces.Runnables.PhantomRaceRunnable;
import org.bukkit.plugin.Plugin;

public class RunnablesRegistry {
    public static void register(Plugin plugin) {
        new PhantomRaceRunnable().runTaskTimer(plugin, 0L, 20L);
    }
}
