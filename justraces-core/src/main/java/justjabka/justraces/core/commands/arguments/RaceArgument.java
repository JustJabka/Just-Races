package justjabka.justraces.core.commands.arguments;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import justjabka.justraces.api.JustRacesAPI;
import justjabka.justraces.api.JustRacesRegistries;
import justjabka.justraces.api.common.definition.RaceDefinition;
import justjabka.justraces.api.common.registry.Registry;
import justjabka.justraces.api.managers.RaceManager;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class RaceArgument extends CustomRegistryArgument<RaceDefinition> {
    private static final DynamicCommandExceptionType ERROR_INVALID_RACE = new DynamicCommandExceptionType(race ->
            MessageComponentSerializer.message().serialize(
                    Component.translatable("argument.race.invalid")
                            .fallback("%s is not a valid race!")
                            .arguments(Component.text(race.toString()))
            ));

    @Override
    public Registry<RaceDefinition> getRegistry() {
        return JustRacesRegistries.RACES;
    }

    @Override
    public RaceDefinition convert(NamespacedKey nativeType) throws CommandSyntaxException {
        NamespacedKey key = NamespacedKey.fromString(nativeType.asString(), JustRacesAPI.getInstance());

        if (key == null) {
            throw ERROR_INVALID_RACE.create(nativeType);
        }

        return RaceManager.getByKey(key);
    }
}
