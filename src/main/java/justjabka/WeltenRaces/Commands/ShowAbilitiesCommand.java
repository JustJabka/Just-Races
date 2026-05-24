package justjabka.WeltenRaces.Commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShowAbilitiesCommand {
    public static LiteralCommandNode<CommandSourceStack> showAbilities() {
        return Commands.literal("showabilities")
                .then(Commands.argument("status", BoolArgumentType.bool())
                        .executes(ctx -> {
                            final CommandSender sender = ctx.getSource().getSender();
                            boolean status = BoolArgumentType.getBool(ctx, "status");

                            if (sender instanceof Player player) {
                                AbilityManager.changeAbilitiesVisibility(player, status);
                            }

                            return status ? 1 : 0;
                        })
                        .then(Commands.argument("target", ArgumentTypes.player())
                                .requires(stack -> stack.getSender().hasPermission("%s.admin".formatted(WeltenRaces.NAMESPACE)))
                                .executes(ctx -> {
                                    final PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("target", PlayerSelectorArgumentResolver.class);
                                    final Player target = targetResolver.resolve(ctx.getSource()).getFirst();

                                    boolean status = BoolArgumentType.getBool(ctx, "status");
                                    AbilityManager.changeAbilitiesVisibility(target, status);

                                    return status ? 1 : 0;
                                })
                        )
                ).build();
    }

    public static void register(Commands registrar) {
        registrar.register(showAbilities());
    }
}
