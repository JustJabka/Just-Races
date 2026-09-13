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
import justjabka.justraces.api.managers.ModifierManager;
import justjabka.justraces.api.modifiers.generic.BaseModifier;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public class ItemModifierArgument implements CustomArgumentType.Converted<BaseModifier, NamespacedKey> {
    private static final DynamicCommandExceptionType ERROR_INVALID_MODIFIER = new DynamicCommandExceptionType(modifier ->
            MessageComponentSerializer.message().serialize(
                    Component.translatable("commands.setrace.invalid_race")
                            .fallback("%s is not a valid item modifier!")
                            .arguments(Component.text(modifier.toString()))
            )); // TODO: change error translate key

    @Override
    public BaseModifier convert(NamespacedKey nativeType) throws CommandSyntaxException {
        NamespacedKey key = NamespacedKey.fromString(nativeType.asString(), JustRacesAPI.getInstance());

        if (key == null) {
            throw ERROR_INVALID_MODIFIER.create(nativeType);
        }

        BaseModifier modifier = ModifierManager.getByKey(key);

        if (modifier == null) {
            throw ERROR_INVALID_MODIFIER.create(nativeType);
        }

        return modifier;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String input = builder.getRemainingLowerCase();

        for (NamespacedKey key : JustRacesRegistries.MODIFIERS.keys()) {
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
