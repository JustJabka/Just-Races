package justjabka.justraces.core.commands.arguments;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import justjabka.justraces.api.JustRacesAPI;
import justjabka.justraces.api.JustRacesRegistries;
import justjabka.justraces.api.abilities.generic.BaseAbility;
import justjabka.justraces.api.common.registry.Registry;
import justjabka.justraces.api.managers.AbilityManager;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class AbilityArgument extends CustomRegistryArgument<BaseAbility> {
    private static final DynamicCommandExceptionType ERROR_INVALID_ABILITY = new DynamicCommandExceptionType(ability ->
            MessageComponentSerializer.message().serialize(
                    Component.translatable("argument.ability.invalid")
                            .fallback("%s is not a valid ability!")
                            .arguments(Component.text(ability.toString()))
            ));

    @Override
    public Registry<BaseAbility> getRegistry() {
        return JustRacesRegistries.ABILITIES;
    }

    @Override
    public BaseAbility convert(NamespacedKey nativeType) throws CommandSyntaxException {
        NamespacedKey key = NamespacedKey.fromString(nativeType.asString(), JustRacesAPI.getInstance());

        if (key == null) {
            throw ERROR_INVALID_ABILITY.create(nativeType);
        }

        return AbilityManager.getByKey(key);
    }
}
