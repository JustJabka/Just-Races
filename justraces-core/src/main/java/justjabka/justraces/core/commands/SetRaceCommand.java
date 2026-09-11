package justjabka.justraces.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import justjabka.justraces.core.commands.arguments.RaceArgument;
import justjabka.justraces.api.events.race.Cause;
import justjabka.justraces.api.definitions.RaceDefinition;
import justjabka.justraces.api.JustRacesAPI;
import justjabka.justraces.api.managers.RaceManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetRaceCommand {
    private static final TranslatableComponent MESSAGE = Component.translatable("commands.setrace.success").fallback("%s became %s");
    private static final TranslatableComponent FAIL_MESSAGE = Component.translatable("commands.setrace.fail").fallback("Failed to change race").color(NamedTextColor.RED);

    public static LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("setrace")
                .requires(stack -> stack.getSender().hasPermission("%s.command.setrace".formatted(JustRacesAPI.NAMESPACE)))
                .then(Commands.argument("target", ArgumentTypes.player())
                        .then(Commands.argument("race", new RaceArgument())
                                .executes(ctx -> {
                                    final PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("target", PlayerSelectorArgumentResolver.class);
                                    final Player target = targetResolver.resolve(ctx.getSource()).getFirst();
                                    CommandSender sender = ctx.getSource().getSender();

                                    RaceDefinition race = ctx.getArgument("race", RaceDefinition.class);

                                    boolean success = RaceManager.setRace(target, race, Cause.COMMAND);
                                    if (!success) {
                                        sender.sendMessage(FAIL_MESSAGE);
                                        return 0;
                                    }

                                    Component raceName = race.getName();
                                    sender.sendMessage(MESSAGE.arguments(target.name(), raceName));

                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .build();
    }

    public static void register(Commands registrar) {
        registrar.register(build());
    }
}
