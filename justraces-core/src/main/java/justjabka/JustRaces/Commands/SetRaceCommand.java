package justjabka.JustRaces.Commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import justjabka.JustRaces.Commands.Arguments.RaceArgument;
import justjabka.JustRaces.Events.Race.PlayerRaceChangePreEvent;
import justjabka.JustRaces.Instances.RaceInstance;
import justjabka.JustRaces.JustRacesAPI;
import justjabka.JustRaces.Managers.RaceManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public class SetRaceCommand {
    private static final TranslatableComponent MESSAGE = Component.translatable("commands.setrace.success").fallback("You became: %s");
    private static final TranslatableComponent FAIL_MESSAGE = Component.translatable("commands.setrace.fail").fallback("Failed to change race").color(NamedTextColor.RED);

    public static LiteralCommandNode<CommandSourceStack> setRace() {
        return Commands.literal("setrace")
                .requires(stack -> stack.getSender().hasPermission("%s.admin".formatted(JustRacesAPI.NAMESPACE)))
                .then(Commands.argument("target", ArgumentTypes.player())
                        .then(Commands.argument("race", new RaceArgument())
                                .executes(ctx -> {
                                    final PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("target", PlayerSelectorArgumentResolver.class);
                                    final Player target = targetResolver.resolve(ctx.getSource()).getFirst();

                                    RaceInstance race = ctx.getArgument("race", RaceInstance.class);

                                    boolean success = RaceManager.setRace(target, race, PlayerRaceChangePreEvent.Cause.COMMAND);
                                    if (!success) {
                                        target.sendMessage(FAIL_MESSAGE);
                                        return 0;
                                    }

                                    Component raceName = race.getName();
                                    target.sendMessage(MESSAGE.arguments(raceName));

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
