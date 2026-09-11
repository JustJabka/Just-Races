package justjabka.justraces.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import justjabka.justraces.api.JustRacesAPI;
import justjabka.justraces.api.events.race.Cause;
import justjabka.justraces.api.managers.RaceManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetRaceCommand {
    private static final TranslatableComponent MESSAGE = Component.translatable("commands.resetrace.success").fallback("Reset race for %s");
    private static final TranslatableComponent FAIL_MESSAGE = Component.translatable("commands.resetrace.fail").fallback("Failed to reset race").color(NamedTextColor.RED);

    public static LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("resetrace")
                .requires(stack -> stack.getSender().hasPermission("%s.command.resetrace".formatted(JustRacesAPI.NAMESPACE)))
                .then(Commands.argument("target", ArgumentTypes.player())
                        .executes(ctx -> {
                            final PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("target", PlayerSelectorArgumentResolver.class);
                            final Player target = targetResolver.resolve(ctx.getSource()).getFirst();
                            CommandSender sender = ctx.getSource().getSender();

                            boolean success = RaceManager.setRace(target, RaceManager.NONE, Cause.COMMAND);
                            if (!success) {
                                sender.sendMessage(FAIL_MESSAGE);
                                return 0;
                            }

                            sender.sendMessage(MESSAGE.arguments(target.name()));

                            return Command.SINGLE_SUCCESS;
                        })
                )
                .build();
    }

    public static void register(Commands registrar) {
        registrar.register(build());
    }
}
