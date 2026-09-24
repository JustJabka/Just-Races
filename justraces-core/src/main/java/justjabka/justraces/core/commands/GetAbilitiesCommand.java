package justjabka.justraces.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import justjabka.justraces.api.JustRacesAPI;
import justjabka.justraces.api.abilities.AbilityTrigger;
import justjabka.justraces.api.abilities.AbilityTriggerCondition;
import justjabka.justraces.api.abilities.generic.BaseAbility;
import justjabka.justraces.api.common.entry.AbilityEntry;
import justjabka.justraces.api.managers.AbilityManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.Set;

public class GetAbilitiesCommand {

    public static LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("getabilities")
                .executes(ctx -> {
                    Player sender = ctx.getSource().getPlayerOrThrow();
                    return getAbilities(sender, ctx);
                })
                .then(Commands.argument("target", ArgumentTypes.player())
                        .requires(stack -> stack.getSender().hasPermission("%s.command.getabilities".formatted(JustRacesAPI.NAMESPACE)))
                        .executes(ctx -> {
                            PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("target", PlayerSelectorArgumentResolver.class);
                            Player target = targetResolver.resolve(ctx.getSource()).getFirst();

                            return getAbilities(target, ctx);
                        })
                )
                .build();
    }

    private static int getAbilities(Player target, CommandContext<CommandSourceStack> ctx) {
        CommandSender sender = ctx.getSource().getSender();

        Set<AbilityEntry> entries = AbilityManager.getAbilityEntriesForPlayer(target);
        Component message = Component.empty();

        for (AbilityEntry entry : entries) {
            String abilityName = getAbilityName(entry);
            String triggerName = getTriggerName(entry);

            message = message
                    .append(Component.text("%s:".formatted(abilityName)))
                    .appendNewline()
                    .append(Component.text("- Trigger: %s".formatted(triggerName)).color(NamedTextColor.GRAY))
                    .appendNewline();

            for (AbilityTriggerCondition condition : entry.conditions()) {
                String conditionName = getConditionName(condition);

                message = message
                        .append(Component.text("- Condition: %s".formatted(conditionName)).color(NamedTextColor.GRAY))
                        .appendNewline();
            }

            message = message.appendNewline();
        }

        sender.sendMessage(message);
        return Command.SINGLE_SUCCESS;
    }

    private static @NonNull String getAbilityName(AbilityEntry entry) {
        BaseAbility ability = entry.ability();
        String name = ability.getKey().getKey();
        return formatName(name);
    }

    private static @NonNull String getTriggerName(AbilityEntry entry) {
        AbilityTrigger trigger = entry.trigger();
        String name = trigger.toString();
        return formatName(name);
    }

    private static @NonNull String getConditionName(AbilityTriggerCondition condition) {
        String name = condition.toString();
        return formatName(name);
    }

    private static @NotNull String formatName(String name) {
        if (name == null || name.isEmpty()) return "";

        String[] words = StringUtils.split(name.toLowerCase(), '_');
        for (int i = 0; i < words.length; i++) {
            words[i] = StringUtils.capitalize(words[i]);
        }

        return StringUtils.join(words, ' ');
    }

    public static void register(Commands registrar) {
        registrar.register(build());
    }
}
