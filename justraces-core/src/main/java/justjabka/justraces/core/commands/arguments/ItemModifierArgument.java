package justjabka.justraces.core.commands.arguments;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import justjabka.justraces.api.JustRacesAPI;
import justjabka.justraces.api.JustRacesRegistries;
import justjabka.justraces.api.interfaces.Registry;
import justjabka.justraces.api.managers.ModifierManager;
import justjabka.justraces.api.modifiers.generic.BaseModifier;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ItemModifierArgument extends CustomRegistryArgument<BaseModifier> {
    private static final DynamicCommandExceptionType ERROR_INVALID_MODIFIER = new DynamicCommandExceptionType(modifier ->
            MessageComponentSerializer.message().serialize(
                    Component.translatable("argument.item_modifier.invalid")
                            .fallback("%s is not a valid item modifier!")
                            .arguments(Component.text(modifier.toString()))
            ));

    @Override
    public Registry<BaseModifier> getRegistry() {
        return JustRacesRegistries.MODIFIERS;
    }

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
}
