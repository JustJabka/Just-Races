package justjabka.justraces.api.types;

import justjabka.justraces.api.abilities.generic.BaseAbility;
import justjabka.justraces.api.traits.generic.Trait;
import justjabka.justraces.api.modifiers.generic.BaseModifier;
import org.bukkit.Material;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public record TransientContainer(
        Map<BaseAbility, Long> abilities,
        Map<Trait, Long> traits,
        Map<Map<BaseModifier, Set<Material>>, Long> itemModifiers
) {
    public static TransientContainer ofDefault() {
        return new TransientContainer(
                new ConcurrentHashMap<>(),
                new ConcurrentHashMap<>(),
                new ConcurrentHashMap<>()
        );
    }
}
