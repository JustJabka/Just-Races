package justjabka.justraces.core;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import justjabka.justraces.core.commands.GetRaceCommand;
import justjabka.justraces.core.commands.JustRacesCommand;
import justjabka.justraces.core.commands.SelectRaceCommand;
import justjabka.justraces.core.commands.SetRaceCommand;

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
            JustRacesCommand.register(registrar);
        });
    }
}