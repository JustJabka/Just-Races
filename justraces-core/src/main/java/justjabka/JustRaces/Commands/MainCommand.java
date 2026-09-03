package justjabka.JustRaces.Commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import justjabka.JustRaces.Interfaces.Configurable.Generic.PluginConfigurable;
import justjabka.JustRaces.JustRacesAPI;
import justjabka.JustRaces.JustRacesRegistries;
import net.kyori.adventure.text.Component;

public class MainCommand {

    public static LiteralCommandNode<CommandSourceStack> mainCommand() {
        return Commands.literal("%s".formatted(JustRacesAPI.NAMESPACE))
                .then(Commands.literal("reload")
                        .executes(ctx -> {
                            JustRacesRegistries.ABILITIES.values().forEach(ability -> {
                                if (!(ability instanceof PluginConfigurable configurable)) return;
                                configurable.reloadConfigFile();
                            });

                            JustRacesRegistries.MODIFIERS.values().forEach(modifier -> {
                                if (!(modifier instanceof PluginConfigurable configurable)) return;
                                configurable.reloadConfigFile();
                            });

                            JustRacesRegistries.TRAITS.values().forEach(trait -> {
                                if (!(trait instanceof PluginConfigurable configurable)) return;
                                configurable.reloadConfigFile();
                            });

                            // TODO: reload races

                            ctx.getSource().getSender().sendMessage(Component.text("Reloaded"));

                            return Command.SINGLE_SUCCESS;
                        })
                )
                .build();
    }

    public static void register(Commands registrar) {
        registrar.register(mainCommand());
    }
}
