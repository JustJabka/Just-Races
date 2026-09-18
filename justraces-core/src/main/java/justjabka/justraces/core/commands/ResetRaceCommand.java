package justjabka.justraces.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import justjabka.justraces.api.JustRacesAPI;
import justjabka.justraces.api.events.race.Cause;
import justjabka.justraces.api.managers.RaceManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetRaceCommand {
    private static final TranslatableComponent MESSAGE = Component.translatable("commands.resetrace.success").fallback("Reset race for %s");

    private static final SimpleCommandExceptionType FAIL_MESSAGE = new SimpleCommandExceptionType(
            MessageComponentSerializer.message().serialize(
                    Component.translatable("commands.resetrace.fail").fallback("Failed to reset race")
            )
    );

    public static LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("resetrace")
                .requires(stack -> stack.getSender().hasPermission("%s.command.resetrace".formatted(JustRacesAPI.NAMESPACE)))
                .executes(ctx -> {
                    Player sender = ctx.getSource().getPlayerOrThrow();
                    return resetRace(sender, ctx);
                })
                .then(Commands.argument("target", ArgumentTypes.player())
                        .executes(ctx -> {
                            PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("target", PlayerSelectorArgumentResolver.class);
                            Player target = targetResolver.resolve(ctx.getSource()).getFirst();

                            return resetRace(target, ctx);
                        })
                )
                .build();
    }

    private static int resetRace(Player target, CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        CommandSender sender = ctx.getSource().getSender();

        boolean success = RaceManager.setRace(target, RaceManager.NONE, Cause.COMMAND);
        if (!success) {
            throw FAIL_MESSAGE.create();
        }

        sender.sendMessage(MESSAGE.arguments(target.name()));

        return Command.SINGLE_SUCCESS;
    }

    public static void register(Commands registrar) {
        registrar.register(build());
    }
}
