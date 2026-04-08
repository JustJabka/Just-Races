package justjabka.weltenRaces.Commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import justjabka.weltenRaces.Commands.Arguments.RaceArgument;
import justjabka.weltenRaces.Managers.RaceManager;
import justjabka.weltenRaces.Types.Race;
import justjabka.weltenRaces.WeltenRaces;
import org.bukkit.entity.Player;

public class SetRaceCommand {
    public static LiteralCommandNode<CommandSourceStack> setRace() {
        return Commands.literal("setrace")
                .requires(stack -> stack.getSender().hasPermission("%s.admin".formatted(WeltenRaces.PLUGIN_ID)))
                .then(Commands.argument("target", ArgumentTypes.player())
                        .then(Commands.argument("race", new RaceArgument())
                                .executes(ctx -> {
                                    final PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("target", PlayerSelectorArgumentResolver.class);
                                    final Player target = targetResolver.resolve(ctx.getSource()).getFirst();

                                    Race race = ctx.getArgument("race", Race.class);

                                    RaceManager.setRace(target, race);

                                    target.sendMessage("You became: " + race);
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .build();
    }

    public static void register(Commands registrar) {
        registrar.register(setRace());
    }
}
