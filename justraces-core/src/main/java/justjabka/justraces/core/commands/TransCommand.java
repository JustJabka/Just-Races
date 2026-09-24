package justjabka.justraces.core.commands;

import com.google.gson.JsonParseException;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.registry.RegistryKey;
import justjabka.justraces.api.JustRacesAPI;
import justjabka.justraces.api.abilities.generic.BaseAbility;
import justjabka.justraces.api.common.entry.AbilityEntry;
import justjabka.justraces.api.common.entry.ItemModifierEntry;
import justjabka.justraces.api.itemmodifiers.generic.BaseItemModifier;
import justjabka.justraces.api.managers.TransientManager;
import justjabka.justraces.api.traits.generic.Trait;
import justjabka.justraces.core.commands.arguments.AbilityArgument;
import justjabka.justraces.core.commands.arguments.ItemModifierArgument;
import justjabka.justraces.core.commands.arguments.TraitArgument;
import justjabka.justraces.core.gson.GsonManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemType;

import java.util.Set;

public class TransCommand {

    private static final DynamicCommandExceptionType ERROR_INVALID_ABILITY_ENTRY = new DynamicCommandExceptionType(
            error -> MessageComponentSerializer.message().serialize(
                    Component.text("Invalid Ability Entry. %s".formatted(error))
            )
    );

    private static final TranslatableComponent MESSAGE_SUCCESS =
            Component.translatable("commands.trans.success").fallback("%s now has %s");

    public static LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("trans")
                .requires(stack -> stack.getSender().hasPermission("%s.command.trans".formatted(JustRacesAPI.NAMESPACE)))
                .then(Commands.argument("target", ArgumentTypes.player())
                        .then(Commands.literal("ability")
                                .then(Commands.argument("ability", new AbilityArgument())
                                        .then(Commands.argument("ticks", LongArgumentType.longArg(0, Long.MAX_VALUE))
                                                .executes(TransCommand::executeAbility
                                                )
                                        )
                                )
                                .then(Commands.argument("ability_entry", StringArgumentType.string())
                                        .then(Commands.argument("ticks", LongArgumentType.longArg(0, Long.MAX_VALUE))
                                                .executes(TransCommand::executeAbilityEntry
                                                )
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
                        .then(Commands.literal("item_modifier")
                                .then(Commands.argument("item_modifier", new ItemModifierArgument())
                                        .then(Commands.argument("item", ArgumentTypes.resource(RegistryKey.ITEM))
                                                .then(Commands.argument("ticks", LongArgumentType.longArg(0, Long.MAX_VALUE))
                                                        .executes(TransCommand::executeItemModifier)
                                                )
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

    private static int executeAbilityEntry(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Player target = resolveTarget(ctx);
        String ability = StringArgumentType.getString(ctx, "ability_entry");
        long ticks = getTicks(ctx);

        try {
            AbilityEntry entry = GsonManager.GSON.fromJson(ability, AbilityEntry.class);

            TransientManager.addTransientAbility(target, entry, ticks);
            sendSuccess(ctx, target, entry.ability().getKey());
            return Command.SINGLE_SUCCESS;
        } catch (JsonParseException e) {
            String cause = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();

            throw ERROR_INVALID_ABILITY_ENTRY.create(cause);
        }
    }

    private static int executeTrait(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Player target = resolveTarget(ctx);
        Trait trait = ctx.getArgument("trait", Trait.class);
        long ticks = getTicks(ctx);

        TransientManager.addTransientTrait(target, trait, ticks);
        sendSuccess(ctx, target, trait.getKey());
        return Command.SINGLE_SUCCESS;
    }

    private static int executeItemModifier(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Player target = resolveTarget(ctx);
        BaseItemModifier modifier = ctx.getArgument("item_modifier", BaseItemModifier.class);
        ItemType item = ctx.getArgument("item", ItemType.class);
        long ticks = getTicks(ctx);

        ItemModifierEntry entry = new ItemModifierEntry(modifier, Set.of(item.createItemStack().getType()));

        TransientManager.addTransientItemModifier(target, entry, ticks);
        sendSuccess(ctx, target, modifier.getKey());
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
