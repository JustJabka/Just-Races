package justjabka.justraces.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import justjabka.justraces.api.JustRacesAPI;
import justjabka.justraces.api.JustRacesRegistries;
import justjabka.justraces.api.abilities.generic.BaseAbility;
import justjabka.justraces.api.common.definition.RaceDefinition;
import justjabka.justraces.api.common.entry.AbilityEntry;
import justjabka.justraces.api.events.race.Cause;
import justjabka.justraces.api.managers.RaceManager;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ObjectComponent;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.object.ObjectContents;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import java.util.*;

public class SelectRaceCommand {
    private static final int DIALOG_COLUMNS = 2;
    private static final int NAVIGATION_ACTIONS_WIDTH = 100;

    private static final Component TITLE = Component.translatable("gui.race_selection.title").fallback("Select Race");

    private static final Component PAGE_PREV = Component.translatable("book.page_button.previous");
    private static final Component PAGE_NEXT = Component.translatable("book.page_button.next");
    private static final Component SELECT = Component.translatable("mco.template.button.select");

    private static final Component ABILITY_BADGE = getBadge("race_selection/ability");
    private static final Component INFO_BADGE = getBadge("race_selection/info");

    private static @NonNull ObjectComponent getBadge(String path) {
        return Component.object(
                ObjectContents.sprite(
                        Key.key(Key.MINECRAFT_NAMESPACE, "gui"),
                        Key.key(JustRacesAPI.NAMESPACE, path)
                )
        );
    }

    public static LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("selectrace")
                .requires(stack -> stack.getSender().hasPermission("%s.command.selectrace".formatted(JustRacesAPI.NAMESPACE)))
                .executes(ctx -> {
                    Player sender = ctx.getSource().getPlayerOrThrow();

                    openRaceDialog(sender, 0);

                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.argument("target", ArgumentTypes.player())
                        .executes(ctx -> {
                            PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("target", PlayerSelectorArgumentResolver.class);
                            Player target = targetResolver.resolve(ctx.getSource()).getFirst();

                            openRaceDialog(target, 0);

                            return Command.SINGLE_SUCCESS;
                        })
                )
                .build();
    }

    private static void handleRaceSelection(Audience audience, RaceDefinition race) {
        if (!(audience instanceof Player player)) return;

        RaceManager.setRace(player, race, Cause.DIALOG);
    }

    private static void openRaceDialog(Player player, int page) {
        Collection<RaceDefinition> races = JustRacesRegistries.RACES.values();

        List<RaceDefinition> raceList = races.stream()
                .filter(instance -> !instance.isHidden())
                .sorted(Comparator.comparing(instance -> instance.getKey().toString()))
                .toList();

        if (raceList.isEmpty()) {
            JustRacesAPI.getLogger().warn("No races registered. Canceling race selection");
            return;
        }

        // Infinite scroll
        if (page < 0) page = raceList.size() - 1;
        if (page >= raceList.size()) page = 0;

        final int currentPage = page;
        RaceDefinition selectedRace = raceList.get(currentPage);

        Dialog dialog = buildDialog(player, selectedRace, currentPage);
        player.showDialog(dialog);
    }

    private static Dialog buildDialog(Player player, RaceDefinition selectedRace, int currentPage) {
        final DialogBase base = DialogBase.builder(TITLE)
                .body(buildDialogBody(selectedRace))
                .pause(true)
                .canCloseWithEscape(false)
                .build();

        final List<ActionButton> navigationActions = List.of(
                ActionButton.builder(PAGE_PREV)
                        .width(NAVIGATION_ACTIONS_WIDTH)
                        .action(changePage(player, currentPage - 1))
                        .build(),
                ActionButton.builder(PAGE_NEXT)
                        .width(NAVIGATION_ACTIONS_WIDTH)
                        .action(changePage(player, currentPage + 1))
                        .build()
        );

        final ActionButton selectAction = ActionButton
                .builder(SELECT)
                .action(DialogAction.customClick(
                        (_, audience) -> handleRaceSelection(audience, selectedRace),
                        singleUseOption()
                ))
                .build();

        return Dialog.create(builder -> builder
                .empty()
                .base(base)
                .type(DialogType.multiAction(navigationActions, selectAction, DIALOG_COLUMNS))
        );
    }

    private static @NonNull List<DialogBody> buildDialogBody(RaceDefinition selectedRace) {
        List<DialogBody> body = new ArrayList<>();

        addRaceTitle(selectedRace, body);
        addRaceAbilities(selectedRace, body);
        addRaceDescription(selectedRace, body);

        return body;
    }

    private static void addRaceTitle(RaceDefinition selectedRace, List<DialogBody> body) {
        Component name = selectedRace.name();
        Component icon = selectedRace.getIcon();

        addHeader(icon, name, body);
    }

    private static void addRaceAbilities(RaceDefinition selectedRace, List<DialogBody> body) {
        Set<AbilityEntry> abilities = selectedRace.getAbilityEntries();

        for (AbilityEntry entry : abilities) {
            BaseAbility ability = entry.ability();

            Component name = ability.name();
            List<Component> description = ability.description();

            addHeader(
                    ABILITY_BADGE,
                    name.decorate(TextDecoration.UNDERLINED),
                    body
            );

            addDescription(description, body);
        }
    }

    private static void addRaceDescription(RaceDefinition selectedRace, List<DialogBody> body) {
        List<Component> description = selectedRace.description();

        addHeader(
                INFO_BADGE,
                Component.text("Information").decorate(TextDecoration.UNDERLINED),
                body
        );
        addDescription(description, body);
    }

    private static void addHeader(Component prefix, Component title, List<DialogBody> body) {
        Component finalTitle = Component.empty()
                .append(prefix)
                .appendSpace()
                .append(title);

        body.add(DialogBody.plainMessage(finalTitle));
    }

    private static void addDescription(List<Component> description, List<DialogBody> body) {
        for (Component line : description) {
            body.add(DialogBody.plainMessage(line.color(NamedTextColor.GRAY)));
        }
    }

    private static DialogAction.@NonNull CustomClickAction changePage(Player player, int page) {
        return DialogAction.customClick(
                (_, _) -> openRaceDialog(player, page),
                singleUseOption()
        );
    }

    private static ClickCallback.Options singleUseOption() {
        return ClickCallback.Options.builder()
                .uses(1)
                .lifetime(ClickCallback.DEFAULT_LIFETIME)
                .build();
    }

    public static void register(Commands registrar) {
        registrar.register(build());
    }
}
