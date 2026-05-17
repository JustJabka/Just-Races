package justjabka.WeltenRaces.Commands.Arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import justjabka.WeltenRaces.Instances.RaceInstance;
import justjabka.WeltenRaces.Registries.RaceRegistry;
import justjabka.WeltenRaces.WeltenRaces;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public class RaceArgument implements CustomArgumentType.Converted<RaceInstance, NamespacedKey> {
    private static final DynamicCommandExceptionType ERROR_INVALID_RACE = new DynamicCommandExceptionType(race ->
            MessageComponentSerializer.message().serialize(
                    Component.translatable("commands.setrace.invalid_race")
                            .fallback("%s is not a valid race!")
                            .arguments(Component.text(race.toString()))
            ));

    @Override
    public RaceInstance convert(NamespacedKey nativeType) throws CommandSyntaxException {
        NamespacedKey key = NamespacedKey.fromString(nativeType.asString(), WeltenRaces.INSTANCE);

        if (key == null) {
            throw ERROR_INVALID_RACE.create(nativeType);
        }

        RaceInstance race = RaceRegistry.getRaces().get(key);

        if (race == null) {
            throw ERROR_INVALID_RACE.create(nativeType);
        }

        return race;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String input = builder.getRemainingLowerCase();

        for (NamespacedKey key : RaceRegistry.getRaces().keySet()) {
            String keyString = key.toString();

            if (keyString.startsWith(input)) {
                builder.suggest(keyString);
            }
        }

        return builder.buildFuture();
    }


    @Override
    public ArgumentType<NamespacedKey> getNativeType() {
        return ArgumentTypes.namespacedKey();
    }
}
