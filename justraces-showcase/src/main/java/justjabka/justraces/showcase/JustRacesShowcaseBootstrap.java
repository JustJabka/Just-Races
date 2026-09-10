package justjabka.justraces.showcase;

import io.papermc.paper.datapack.DatapackRegistrar;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@SuppressWarnings("UnstableApiUsage")
public class JustRacesShowcaseBootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(BootstrapContext context) {
        final LifecycleEventManager<BootstrapContext> manager = context.getLifecycleManager();

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
