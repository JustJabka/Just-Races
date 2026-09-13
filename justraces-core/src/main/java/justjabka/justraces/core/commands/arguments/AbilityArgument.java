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
import justjabka.justraces.api.abilities.generic.BaseAbility;
import justjabka.justraces.api.managers.AbilityManager;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public class AbilityArgument implements CustomArgumentType.Converted<BaseAbility, NamespacedKey> {
    private static final DynamicCommandExceptionType ERROR_INVALID_ABILITY = new DynamicCommandExceptionType(ability ->
            MessageComponentSerializer.message().serialize(
                    Component.translatable("commands.setrace.invalid_race")
                            .fallback("%s is not a valid ability!")
                            .arguments(Component.text(ability.toString()))
            )); // TODO: change error message

    @Override
    public BaseAbility convert(NamespacedKey nativeType) throws CommandSyntaxException {
        NamespacedKey key = NamespacedKey.fromString(nativeType.asString(), JustRacesAPI.getInstance());

        if (key == null) {
            throw ERROR_INVALID_ABILITY.create(nativeType);
        }

        BaseAbility ability = AbilityManager.getByKey(key);

        if (ability == null) {
            throw ERROR_INVALID_ABILITY.create(nativeType);
        }

        return ability;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String input = builder.getRemainingLowerCase();

        for (NamespacedKey key : JustRacesRegistries.ABILITIES.keys()) {
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
