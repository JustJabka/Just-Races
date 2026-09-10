package justjabka.justraces.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import justjabka.justraces.api.JustRacesAPI;
import justjabka.justraces.api.JustRacesRegistries;
import justjabka.justraces.api.interfaces.Registry;
import justjabka.justraces.api.interfaces.ReloadableRegistry;
import justjabka.justraces.api.managers.RaceManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JustRacesCommand {
    private static final Component RELOAD_START_MESSAGE = Component.translatable("commands.justraces.reload.start").fallback("Reloading!");
    private static final Component RELOAD_SUCCESS_MESSAGE = Component.translatable("commands.justraces.reload.success").fallback("Successfully Reloaded!");

    public static LiteralCommandNode<CommandSourceStack> justraces() {
        return Commands.literal("%s".formatted(JustRacesAPI.NAMESPACE))
                .then(Commands.literal("reload")
                        .requires(stack -> stack.getSender().hasPermission("%s.command.justraces.reload".formatted(JustRacesAPI.NAMESPACE)))
                        .executes(ctx -> {
                            CommandSender sender = ctx.getSource().getSender();

                            sender.sendMessage(RELOAD_START_MESSAGE);

                            reloadAll(sender);

                            return Command.SINGLE_SUCCESS;
                        })
                )
                .build();
    }

    private static void reloadAll(CommandSender sender) {
        reload(JustRacesRegistries.ABILITIES);
        reload(JustRacesRegistries.TRAITS);
        reload(JustRacesRegistries.MODIFIERS);
        reload(JustRacesRegistries.RACES);

        for (Player player : Bukkit.getOnlinePlayers()) {
            RaceManager.reloadRace(player);
        }

        sender.sendMessage(RELOAD_SUCCESS_MESSAGE);
    }

    private static void reload(Registry<?> registry) {
        if (!(registry instanceof ReloadableRegistry reloadable)) return;
        reloadable.reload();
    }

    public static void register(Commands registrar) {
        registrar.register(justraces());
    }
}
