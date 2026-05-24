package justjabka.JustRaces;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.datapack.DatapackRegistrar;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import justjabka.JustRaces.Commands.GetRaceCommand;
import justjabka.JustRaces.Commands.SelectRaceCommand;
import justjabka.JustRaces.Commands.SetRaceCommand;
import justjabka.JustRaces.Commands.ShowAbilitiesCommand;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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
            ShowAbilitiesCommand.register(registrar);
        });

        manager.registerEventHandler(LifecycleEvents.DATAPACK_DISCOVERY.newHandler(
                event -> {
                    final DatapackRegistrar registrar = event.registrar();

                    try {
                        // Retrieve the URI of the datapack folder.
                        URI uri = this.getClass().getResource("/datapack").toURI();
                        // Discover the pack. The ID is set to "provided", which indicates to
                        // a server owner that your plugin includes this data pack.
                        registrar.discoverPack(uri, "provided");
                    } catch (URISyntaxException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        ));
    }
}