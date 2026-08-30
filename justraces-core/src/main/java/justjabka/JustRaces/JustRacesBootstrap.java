package justjabka.JustRaces;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import justjabka.JustRaces.Commands.GetRaceCommand;
import justjabka.JustRaces.Commands.MainCommand;
import justjabka.JustRaces.Commands.SelectRaceCommand;
import justjabka.JustRaces.Commands.SetRaceCommand;

@SuppressWarnings("UnstableApiUsage")
public class JustRacesBootstrap implements PluginBootstrap {
    @Override
    public void bootstrap(BootstrapContext context) {
        final LifecycleEventManager<BootstrapContext> manager = context.getLifecycleManager();

        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands registrar = event.registrar();

            SetRaceCommand.register(registrar);
            GetRaceCommand.register(registrar);
            SelectRaceCommand.register(registrar);
            MainCommand.register(registrar);
        });
    }
}