package justjabka.justraces.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import justjabka.justraces.api.JustRacesAPI;
import justjabka.justraces.api.abilities.generic.BaseAbility;
import justjabka.justraces.api.managers.TransientManager;
import justjabka.justraces.api.traits.generic.Trait;
import justjabka.justraces.core.commands.arguments.AbilityArgument;
import justjabka.justraces.core.commands.arguments.TraitArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

public class TransCommand {

    private static final TranslatableComponent MESSAGE_SUCCESS =
            Component.translatable("commands.trans.success").fallback("%s now has %s");

    public static LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("trans")
                .requires(stack -> stack.getSender().hasPermission("%s.command.trans".formatted(JustRacesAPI.NAMESPACE)))
                .then(Commands.argument("target", ArgumentTypes.player())
                        .then(Commands.literal("ability")
                                .then(Commands.argument("ability", new AbilityArgument())
                                        .then(Commands.argument("ticks", LongArgumentType.longArg(0, Long.MAX_VALUE))
                                                .executes(TransCommand::executeAbility)
                                        )
                                )
                        )
                        .then(Commands.literal("trait")
                                .then(Commands.argument("trait", new TraitArgument())
                                        .then(Commands.argument("ticks", LongArgumentType.longArg(0, Long.MAX_VALUE))
                                                .executes(TransCommand::executeTrait)
                                        )
                                )
                        )
                )
                .build();
    }

    private static int executeAbility(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Player target = resolveTarget(ctx);
        BaseAbility ability = ctx.getArgument("ability", BaseAbility.class);
        long ticks = getTicks(ctx);

        TransientManager.addTransientAbility(target, ability, ticks);
        sendSuccess(ctx, target, ability.getKey());
        return Command.SINGLE_SUCCESS;
    }

    private static int executeTrait(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Player target = resolveTarget(ctx);
        Trait trait = ctx.getArgument("trait", Trait.class);
        long ticks = getTicks(ctx);

        TransientManager.addTransientTrait(target, trait, ticks);
        sendSuccess(ctx, target, trait.getKey());
        return Command.SINGLE_SUCCESS;
    }

    private static Player resolveTarget(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        PlayerSelectorArgumentResolver resolver = ctx.getArgument("target", PlayerSelectorArgumentResolver.class);
        return resolver.resolve(ctx.getSource()).getFirst();
    }

    private static void sendSuccess(CommandContext<CommandSourceStack> ctx, Player target, NamespacedKey key) {
        ctx.getSource().getSender().sendMessage(MESSAGE_SUCCESS.arguments(target.name(), Component.text(key.asString())));
    }

    private static long getTicks(CommandContext<CommandSourceStack> ctx) {
        return LongArgumentType.getLong(ctx, "ticks");
    }

    public static void register(Commands registrar) {
        registrar.register(build());
    }
}
