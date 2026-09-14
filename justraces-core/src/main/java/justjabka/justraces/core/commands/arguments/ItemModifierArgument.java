package justjabka.justraces.core.commands.arguments;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import justjabka.justraces.api.JustRacesAPI;
import justjabka.justraces.api.JustRacesRegistries;
import justjabka.justraces.api.common.registry.Registry;
import justjabka.justraces.api.managers.ItemModifierManager;
import justjabka.justraces.api.itemmodifiers.generic.BaseItemModifier;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ItemModifierArgument extends CustomRegistryArgument<BaseItemModifier> {
    private static final DynamicCommandExceptionType ERROR_INVALID_MODIFIER = new DynamicCommandExceptionType(modifier ->
            MessageComponentSerializer.message().serialize(
                    Component.translatable("argument.item_modifier.invalid")
                            .fallback("%s is not a valid item modifier!")
                            .arguments(Component.text(modifier.toString()))
            ));

    @Override
    public Registry<BaseItemModifier> getRegistry() {
        return JustRacesRegistries.ITEM_MODIFIERS;
    }

    @Override
    public BaseItemModifier convert(NamespacedKey nativeType) throws CommandSyntaxException {
        NamespacedKey key = NamespacedKey.fromString(nativeType.asString(), JustRacesAPI.getInstance());

        if (key == null) {
            throw ERROR_INVALID_MODIFIER.create(nativeType);
        }

        BaseItemModifier modifier = ItemModifierManager.getByKey(key);

        if (modifier == null) {
            throw ERROR_INVALID_MODIFIER.create(nativeType);
        }

        return modifier;
    }
}
