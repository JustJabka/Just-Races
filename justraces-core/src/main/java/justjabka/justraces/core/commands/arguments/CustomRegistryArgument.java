package justjabka.justraces.core.commands.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import justjabka.justraces.api.common.registry.Registry;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public abstract class CustomRegistryArgument<T> implements CustomArgumentType.Converted<T, NamespacedKey> {

    public abstract Registry<T> getRegistry();

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String remaining = builder.getRemainingLowerCase();

        for (NamespacedKey key : getRegistry().keys()) {
            String fullKey = key.toString().toLowerCase();
            String keyPath = key.getKey().toLowerCase();

            boolean matchesByKey = fullKey.startsWith(remaining);
            boolean matchesByKeyPath = keyPath.startsWith(remaining);

            if (matchesByKey || matchesByKeyPath) {
                builder.suggest(fullKey);
            }
        }

        return builder.buildFuture();
    }


    @Override
    public ArgumentType<NamespacedKey> getNativeType() {
        return ArgumentTypes.namespacedKey();
    }
}
