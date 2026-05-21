package justjabka.WeltenRaces.Registries;

import justjabka.WeltenRaces.Runnables.GlobalRunnable;
import justjabka.WeltenRaces.Runnables.Race.*;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.plugin.Plugin;

public class RunnablesRegistry {
    public static void register(Plugin plugin, ConfigRegistry configs) {
        new GlobalRunnable().runTaskTimer(plugin, 0L, 10L);
        new PhantomRaceRunnable(configs.phantomRaceConfig).runTaskTimer(plugin, 0L, 20L);
        new SkyzernRaceRunnable(configs.skyzernRaceConfig).runTaskTimer(plugin, 0L, 20L);
        new EpiphyteRaceRunnable(configs.epiphyteRaceConfig).runTaskTimer(plugin, 0L, 20L);
        new LizardRaceRunnable(configs.lizardRaceConfig).runTaskTimer(plugin, 0L, 20L);
        new FetrRaceRunnable(configs.fetrRaceConfig).runTaskTimer(plugin, 0L, 20L);

        WeltenRaces.LOGGER.info("Successfully registered runnables!");
    }
}
