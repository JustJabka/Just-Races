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
import justjabka.justraces.api.events.race.Cause;
import justjabka.justraces.api.definitions.RaceDefinition;
import justjabka.justraces.api.JustRacesAPI;
import justjabka.justraces.api.JustRacesRegistries;
import justjabka.justraces.api.managers.RaceManager;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class SelectRaceCommand {
    private static final int DIALOG_COLUMNS = 2;
    private static final int NAVIGATION_ACTIONS_WIDTH = 100;

    private static final Component RACE_SELECTION_TITLE = Component.translatable("gui.race_selection.title").fallback("Select Race");

    private static final Component RACE_SELECTION_PAGE_PREV = Component.translatable("book.page_button.previous");
    private static final Component RACE_SELECTION_PAGE_NEXT = Component.translatable("book.page_button.next");
    private static final Component RACE_SELECTION_SELECT = Component.translatable("mco.template.button.select");

    public static LiteralCommandNode<CommandSourceStack> selectRace() {
        return Commands.literal("selectrace")
                .requires(stack -> stack.getSender().hasPermission("%s.command.selectrace".formatted(JustRacesAPI.NAMESPACE)))
                .then(Commands.argument("target", ArgumentTypes.player())
                        .executes(ctx -> {
                            final PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("target", PlayerSelectorArgumentResolver.class);
                            final Player target = targetResolver.resolve(ctx.getSource()).getFirst();

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
        List<DialogBody> body = new ArrayList<>();

        Component selectedRaceName = selectedRace.getName();
        List<Component> selectedRaceDescription = selectedRace.getDescription();
        Component selectedRaceIcon = selectedRace.getIcon();

        Component selectedRaceTitle = Component.empty() // using empty component to prevent icon from mutating race name properties
                .append(selectedRaceIcon, selectedRaceName);

        body.add(DialogBody.plainMessage(selectedRaceTitle));
        for (Component line : selectedRaceDescription) {
            body.add(DialogBody.plainMessage(line));
        }

        DialogBase base = DialogBase.builder(RACE_SELECTION_TITLE)
                .body(body)
                .pause(true)
                .canCloseWithEscape(false)
                .build();

        List<ActionButton> navigationActions = List.of(
                ActionButton.builder(RACE_SELECTION_PAGE_PREV)
                        .width(NAVIGATION_ACTIONS_WIDTH)
                        .action(changePage(player, currentPage - 1))
                        .build(),
                ActionButton.builder(RACE_SELECTION_PAGE_NEXT)
                        .width(NAVIGATION_ACTIONS_WIDTH)
                        .action(changePage(player, currentPage + 1))
                        .build()
        );

        ActionButton selectAction = ActionButton
                .builder(RACE_SELECTION_SELECT)
                .action(DialogAction.customClick(
                        (_, audience) -> handleRaceSelection(audience, selectedRace),
                        singeUseOption()
                ))
                .build();

        return Dialog.create(builder -> builder
                .empty()
                .base(base)
                .type(DialogType.multiAction(navigationActions, selectAction, DIALOG_COLUMNS))
        );
    }

    private static DialogAction.@NonNull CustomClickAction changePage(Player player, int page) {
        return DialogAction.customClick(
                (_, _) -> openRaceDialog(player, page),
                singeUseOption()
        );
    }

    private static ClickCallback.Options singeUseOption() {
        return ClickCallback.Options.builder()
                .uses(1)
                .lifetime(ClickCallback.DEFAULT_LIFETIME)
                .build();
    }

    public static void register(Commands registrar) {
        registrar.register(selectRace());
    }
}
