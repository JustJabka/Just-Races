package justjabka.justraces.core.commands.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import justjabka.justraces.api.JustRacesAPI;
import justjabka.justraces.api.JustRacesRegistries;
import justjabka.justraces.api.interfaces.Trait;
import justjabka.justraces.api.managers.TraitManager;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public class TraitArgument implements CustomArgumentType.Converted<Trait, NamespacedKey> {
    private static final DynamicCommandExceptionType ERROR_INVALID_TRAIT = new DynamicCommandExceptionType(trait ->
            MessageComponentSerializer.message().serialize(
                    Component.translatable("commands.setrace.invalid_race")
                            .fallback("%s is not a valid trait!")
                            .arguments(Component.text(trait.toString()))
            )); // TODO: change error translate key

    @Override
    public Trait convert(NamespacedKey nativeType) throws CommandSyntaxException {
        NamespacedKey key = NamespacedKey.fromString(nativeType.asString(), JustRacesAPI.getInstance());

        if (key == null) {
            throw ERROR_INVALID_TRAIT.create(nativeType);
        }

        Trait trait = TraitManager.getByKey(key);

        if (trait == null) {
            throw ERROR_INVALID_TRAIT.create(nativeType);
        }

        return trait;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String input = builder.getRemainingLowerCase();

        for (NamespacedKey key : JustRacesRegistries.TRAITS.keys()) {
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
