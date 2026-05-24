package justjabka.WeltenRaces.Commands;

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
import justjabka.WeltenRaces.Instances.RaceInstance;
import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.Registries.RacesRegistry;
import justjabka.WeltenRaces.WeltenRaces;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class SelectRaceCommand {
    private static final int dialogColumns = 2;
    private static final int navigationActionsWidth = 100;

    public static LiteralCommandNode<CommandSourceStack> selectRace() {
        return Commands.literal("selectrace")
                .requires(stack -> stack.getSender().hasPermission("%s.admin".formatted(WeltenRaces.NAMESPACE)))
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

    private static void handleRaceSelection(Audience audience, RaceInstance race) {
        if (!(audience instanceof Player player)) return;

        RaceManager.setRace(player, race.getKey());
    }

    private static void openRaceDialog(Player player, int page) {
        List<RaceInstance> races = new ArrayList<>(RacesRegistry.getRaces().values());

        if (races.isEmpty()) {
            WeltenRaces.LOGGER.warn("No races registered. Canceling race selection");
            return;
        }

        // Infinite scroll
        if (page < 0) page = races.size() - 1;
        if (page >= races.size()) page = 0;

        final int currentPage = page;
        RaceInstance selectedRace = races.get(currentPage);

        List<DialogBody> body = List.of(
                DialogBody.plainMessage(selectedRace.getName()),
                DialogBody.plainMessage(Component.text("Race description")) // TODO: add description to RaceInstance
        );

        DialogBase base = DialogBase.builder(Component.text("Select Race"))
                .body(body)
                .pause(true)
                .canCloseWithEscape(false)
                .build();

        List<ActionButton> navigationActions = List.of(
                ActionButton.builder(Component.translatable("book.page_button.previous"))
                        .width(navigationActionsWidth)
                        .action(changePage(player, currentPage - 1))
                        .build(),
                ActionButton.builder(Component.translatable("book.page_button.next"))
                        .width(navigationActionsWidth)
                        .action(changePage(player, currentPage + 1))
                        .build()
        );

        ActionButton selectAction = ActionButton
                .builder(Component.translatable("mco.template.button.select"))
                .action(DialogAction.customClick(
                        (view, audience) -> handleRaceSelection(audience, selectedRace),
                        singeUseOption()
                ))
                .build();

        Dialog dialog = Dialog.create(builder -> builder
                .empty()
                .base(base)
                .type(DialogType.multiAction(navigationActions, selectAction, dialogColumns))
        );

        player.showDialog(dialog);
    }

    private static DialogAction.@NonNull CustomClickAction changePage(Player player, int page) {
        return DialogAction.customClick(
                (view, audience) -> openRaceDialog(player, page),
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
