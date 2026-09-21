package justjabka.justraces.core;

import justjabka.justraces.api.JustRacesAPI;
import justjabka.justraces.api.JustRacesRegistries;
import justjabka.justraces.api.common.configurable.PluginConfigurable;
import justjabka.justraces.core.registries.ListenersRegistry;
import justjabka.justraces.core.registries.RunnablesRegistry;
import justjabka.justraces.api.traits.generic.BaseTraitRunnable;
import justjabka.justraces.core.registries.impl.AbilitiesRegistry;
import justjabka.justraces.core.registries.impl.ItemModifiersRegistry;
import justjabka.justraces.core.registries.impl.RacesRegistry;
import justjabka.justraces.core.registries.impl.TraitsRegistry;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class JustRaces extends JavaPlugin {

    private final AbilitiesRegistry abilitiesRegistry = new AbilitiesRegistry();
    private final TraitsRegistry traitsRegistry = new TraitsRegistry();
    private final ItemModifiersRegistry itemModifiersRegistry = new ItemModifiersRegistry();
    private final RacesRegistry racesRegistry = new RacesRegistry();

    @Override
    public void onEnable() {
        // Add Hooks
        JustRacesRegistries.ABILITIES.addHook((_, ability) -> {
            Bukkit.getPluginManager().registerEvents(ability, JustRacesAPI.getInstance());

            if (ability instanceof PluginConfigurable configurable) {
                configurable.initializeConfigFile();
            }
        });

        JustRacesRegistries.ITEM_MODIFIERS.addHook((_, modifier) -> {
            if (modifier instanceof Listener listener) {
                Bukkit.getPluginManager().registerEvents(listener, JustRacesAPI.getInstance());
            }

            if (modifier instanceof PluginConfigurable configurable) {
                configurable.initializeConfigFile();
            }
        });

        JustRacesRegistries.TRAITS.addHook((_, trait) -> {
            if (trait instanceof Listener listener) {
                Bukkit.getPluginManager().registerEvents(listener, JustRacesAPI.getInstance());
            }

            if (trait instanceof BaseTraitRunnable runnable) {
                runnable.runTaskTimer(JustRacesAPI.getInstance(), 0L, runnable.getTickPeriod());
            }

            if (trait instanceof PluginConfigurable configurable) {
                configurable.initializeConfigFile();
            }
        });

        // Register
        Bukkit.getScheduler().runTask(this, () -> {
            racesRegistry.reload();

            ListenersRegistry.register(this);
            RunnablesRegistry.register(this);
        });
    }

    @Override
    public void onLoad() {
        JustRacesAPI.init(
                this,
                getSLF4JLogger(),
                racesRegistry,
                abilitiesRegistry,
                traitsRegistry,
                itemModifiersRegistry
        );
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }
}