package justjabka.WeltenRaces;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import justjabka.WeltenRaces.Commands.SetRaceCommand;

@SuppressWarnings("UnstableApiUsage")
public class WeltenRacesBootstrap implements PluginBootstrap {
    @Override
    public void bootstrap(BootstrapContext context) {
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands registrar = event.registrar();

            SetRaceCommand.register(registrar);
        });
    }
}