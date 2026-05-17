package justjabka.WeltenRaces.Commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.Types.Race;
import justjabka.WeltenRaces.WeltenRaces;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.entity.Player;

public class GetRaceCommand {
    private static final TranslatableComponent message = Component.translatable("commands.getrace.success").fallback("Your race: %s");

    public static LiteralCommandNode<CommandSourceStack> getRace() {
        return Commands.literal("getrace")
                .requires(stack -> stack.getSender().hasPermission("%s.admin".formatted(WeltenRaces.NAMESPACE)))
                .then(Commands.argument("target", ArgumentTypes.player())
                        .executes(ctx -> {
                            final PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("target", PlayerSelectorArgumentResolver.class);
                            final Player target = targetResolver.resolve(ctx.getSource()).getFirst();

                            Race race = RaceManager.getRace(target);
                            TextComponent raceName = Component.text(race.toString());

                            target.sendMessage(message.arguments(raceName));

                            return Command.SINGLE_SUCCESS;
                        })
                )
                .build();
    }

    public static void register(Commands registrar) {
        registrar.register(getRace());
    }
}
