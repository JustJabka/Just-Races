package justjabka.JustRaces.Commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import justjabka.JustRaces.Interfaces.Configurable.Generic.PluginConfigurable;
import justjabka.JustRaces.JustRacesAPI;
import justjabka.JustRaces.JustRacesRegistries;
import justjabka.JustRaces.Managers.RaceManager;
import justjabka.JustRaces.Registries.RacesRegistry;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JustRacesCommand {

    public static LiteralCommandNode<CommandSourceStack> justraces() {
        return Commands.literal("%s".formatted(JustRacesAPI.NAMESPACE))
                .then(Commands.literal("reload")
                        .executes(ctx -> {
                            CommandSender sender = ctx.getSource().getSender();

                            sender.sendMessage(Component.text("Reloading!"));

                            reloadAbilities(sender);
                            reloadModifiers(sender);
                            reloadTraits(sender);
                            reloadRaces(sender);

                            return Command.SINGLE_SUCCESS;
                        })
                )
                .build();
    }

    private static void reloadAbilities(CommandSender sender) {
        JustRacesRegistries.ABILITIES.values().forEach(ability -> {
            if (!(ability instanceof PluginConfigurable configurable)) return;
            configurable.reloadConfigFile();
        });

        sender.sendMessage(Component.text("Reloaded %s abilities".formatted(JustRacesRegistries.ABILITIES.values().size())));
    }

    private static void reloadModifiers(CommandSender sender) {
        JustRacesRegistries.MODIFIERS.values().forEach(modifier -> {
            if (!(modifier instanceof PluginConfigurable configurable)) return;
            configurable.reloadConfigFile();
        });

        sender.sendMessage(Component.text("Reloaded %s modifiers".formatted(JustRacesRegistries.MODIFIERS.values().size())));
    }

    private static void reloadTraits(CommandSender sender) {
        JustRacesRegistries.TRAITS.values().forEach(trait -> {
            if (!(trait instanceof PluginConfigurable configurable)) return;
            configurable.reloadConfigFile();
        });

        sender.sendMessage(Component.text("Reloaded %s traits".formatted(JustRacesRegistries.TRAITS.values().size())));
    }

    private static void reloadRaces(CommandSender sender) {
        // Reload Races Definitions
        RacesRegistry.reloadAllRaces();

        // Reapply Races for All Players
        for (Player player : Bukkit.getOnlinePlayers()) {
            RaceManager.reloadRace(player);
        }

        sender.sendMessage(Component.text("Reloaded %s races".formatted(JustRacesRegistries.RACES.values().size())));
    }

    public static void register(Commands registrar) {
        registrar.register(justraces());
    }
}
