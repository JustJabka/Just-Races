package justjabka.WeltenRaces.Commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import justjabka.WeltenRaces.Commands.Arguments.RaceArgument;
import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.Types.Race;
import justjabka.WeltenRaces.WeltenRaces;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.entity.Player;

public class SetRaceCommand {
    private static final TranslatableComponent message = Component.translatable("commands.setrace.success").fallback("You became: %s");

    public static LiteralCommandNode<CommandSourceStack> setRace() {
        return Commands.literal("setrace")
                .requires(stack -> stack.getSender().hasPermission("%s.admin".formatted(WeltenRaces.NAMESPACE)))
                .then(Commands.argument("target", ArgumentTypes.player())
                        .then(Commands.argument("race", new RaceArgument())
                                .executes(ctx -> {
                                    final PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("target", PlayerSelectorArgumentResolver.class);
                                    final Player target = targetResolver.resolve(ctx.getSource()).getFirst();

                                    Race race = ctx.getArgument("race", Race.class);
                                    Component raceName = Component.text(race.toString());

                                    RaceManager.setRace(target, race);

                                    target.sendMessage(message.arguments(raceName));

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
