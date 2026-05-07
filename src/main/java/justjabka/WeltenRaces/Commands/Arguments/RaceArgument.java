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
import justjabka.WeltenRaces.Types.Race;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@NullMarked
public class RaceArgument implements CustomArgumentType.Converted<Race, String> {
    private static final DynamicCommandExceptionType ERROR_INVALID_RACE = new DynamicCommandExceptionType(race ->
            MessageComponentSerializer.message().serialize(
                    Component.translatable("commands.setrace.invalid_race")
                            .fallback("%s is not a valid race!")
                            .arguments(Component.text(race.toString()))
            ));

    @Override
    public Race convert(String nativeType) throws CommandSyntaxException {
        try {
            return Race.valueOf(nativeType.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ignored) {
            throw ERROR_INVALID_RACE.create(nativeType);
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (Race race : Race.values()) {
            String name = race.toString();

            // Only suggest if the race name matches the user input
            if (name.startsWith(builder.getRemainingLowerCase())) {
                builder.suggest(race.toString());
            }
        }

        return builder.buildFuture();
    }


    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }
}
