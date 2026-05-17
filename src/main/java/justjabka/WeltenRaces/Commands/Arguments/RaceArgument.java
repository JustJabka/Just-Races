package justjabka.WeltenRaces.Commands.Arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import justjabka.WeltenRaces.Instances.RaceInstance;
import justjabka.WeltenRaces.Registries.RaceRegistry;
import justjabka.WeltenRaces.WeltenRaces;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public class RaceArgument implements CustomArgumentType.Converted<RaceInstance, String> {
    private static final DynamicCommandExceptionType ERROR_INVALID_RACE = new DynamicCommandExceptionType(race ->
            MessageComponentSerializer.message().serialize(
                    Component.translatable("commands.setrace.invalid_race")
                            .fallback("%s is not a valid race!")
                            .arguments(Component.text(race.toString()))
            ));

    @Override
    public RaceInstance convert(String nativeType) throws CommandSyntaxException {
        NamespacedKey key = NamespacedKey.fromString(nativeType.toLowerCase(), WeltenRaces.INSTANCE);

        if (key == null) {
            throw ERROR_INVALID_RACE.create(nativeType);
        }

        RaceInstance race = RaceRegistry.getRegisteredRaces().get(key);

        if (race == null) {
            throw ERROR_INVALID_RACE.create(nativeType);
        }

        return race;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String input = builder.getRemainingLowerCase();

        for (NamespacedKey key : RaceRegistry.getRegisteredRaces().keySet()) {
            String fullKey = key.toString();
            String shortKey = key.getKey();

            if (input.contains(String.valueOf(NamespacedKey.DEFAULT_SEPARATOR))) {
                if (fullKey.startsWith(input)) {
                    builder.suggest(fullKey);
                }
            } else {
                if (shortKey.startsWith(input)) {
                    builder.suggest(shortKey);
                }
            }
        }

        return builder.buildFuture();
    }


    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }
}
