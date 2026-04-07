package justjabka.weltenRaces;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import justjabka.weltenRaces.Manager.RaceManager;
import org.bukkit.entity.Player;

@SuppressWarnings("UnstableApiUsage")
public class WeltenRacesBootstrap implements PluginBootstrap {
    public static final String[] races = {"armat", "epiphyte", "lizard", "phantom", "skyzern"};

    @Override
    public void bootstrap(BootstrapContext context) {
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();

            commands.register(
                    Commands.literal("setrace")
                            .requires(stack -> stack.getSender().hasPermission(WeltenRaces.PLUGIN_ID + "admin"))
                            .then(Commands.argument("target", ArgumentTypes.player())
                                    .then(Commands.argument("race", StringArgumentType.word())
                                            .suggests((ctx, builder) -> {
                                                for (String race : races) {
                                                    builder.suggest(race);
                                                }
                                                return builder.buildFuture();
                                            })
                                            .executes(ctx -> {
                                                Player target = ctx.getArgument("target", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource()).getFirst();
                                                String raceId = StringArgumentType.getString(ctx, "race");

                                                return setRace(target, raceId);
                                            })
                                    )
                            )
                            .build()
            );
        });
    }

    private int setRace(Player player, String raceId) {
        boolean exists = false;

        for (String race : races) {
            if (race.equalsIgnoreCase(raceId)) {
                exists = true;
                break;
            }
        }

        if (!exists) return 0;

        RaceManager.setRace(player, raceId);

        player.sendMessage("You became: " + raceId);
        return Command.SINGLE_SUCCESS;
    }
}