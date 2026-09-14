package justjabka.justraces.core.commands.arguments;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import justjabka.justraces.api.JustRacesAPI;
import justjabka.justraces.api.JustRacesRegistries;
import justjabka.justraces.api.common.registry.Registry;
import justjabka.justraces.api.managers.TraitManager;
import justjabka.justraces.api.traits.generic.Trait;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class TraitArgument extends CustomRegistryArgument<Trait> {
    private static final DynamicCommandExceptionType ERROR_INVALID_TRAIT = new DynamicCommandExceptionType(trait ->
            MessageComponentSerializer.message().serialize(
                    Component.translatable("argument.trait.invalid")
                            .fallback("%s is not a valid trait!")
                            .arguments(Component.text(trait.toString()))
            ));

    @Override
    public Registry<Trait> getRegistry() {
        return JustRacesRegistries.TRAITS;
    }

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
}
