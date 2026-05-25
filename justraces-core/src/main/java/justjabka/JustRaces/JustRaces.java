package justjabka.JustRaces;

import justjabka.JustRaces.Registries.*;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class JustRaces extends JavaPlugin {

    @Override
    public void onEnable() {
        ConfigRegistry configs = new ConfigRegistry(this);

        RacesRegistry.register(this);
        ListenersRegistry.register(this);
        RunnablesRegistry.register(this);
        AbilitiesRegistry.register(this, configs);
        ModifiersRegistry.register(this, configs);
    }

    @Override
    public void onLoad() {
        JustRacesAPI.init(this, getSLF4JLogger());

        DatapackRegistry.register(this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }
}