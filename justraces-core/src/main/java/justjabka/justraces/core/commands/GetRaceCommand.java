package justjabka.justraces.core.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import justjabka.justraces.api.common.definition.RaceDefinition;
import justjabka.justraces.api.JustRacesAPI;
import justjabka.justraces.api.managers.RaceManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetRaceCommand {
    private static final TranslatableComponent MESSAGE = Component.translatable("commands.getrace.success").fallback("%s's race is %s");

    public static LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("getrace")
                .requires(stack -> stack.getSender().hasPermission("%s.command.getrace".formatted(JustRacesAPI.NAMESPACE)))
                .executes(ctx -> {
                    Player sender = ctx.getSource().getPlayerOrThrow();
                    return getRace(sender, ctx);
                })
                .then(Commands.argument("target", ArgumentTypes.player())
                        .executes(ctx -> {
                            PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("target", PlayerSelectorArgumentResolver.class);
                            Player target = targetResolver.resolve(ctx.getSource()).getFirst();

                            return getRace(target, ctx);
                        })
                )
                .build();
    }

    private static int getRace(Player target, CommandContext<CommandSourceStack> ctx) {
        CommandSender sender = ctx.getSource().getSender();

        RaceDefinition race = RaceManager.getRace(target);
        Component raceName = race.getName();

        sender.sendMessage(MESSAGE.arguments(target.name(), raceName));

        return !race.equals(RaceManager.NONE) ? 1 : 0;
    }

    public static void register(Commands registrar) {
        registrar.register(build());
    }
}
