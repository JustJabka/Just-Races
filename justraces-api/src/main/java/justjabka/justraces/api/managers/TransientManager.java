package justjabka.justraces.api.managers;

import justjabka.justraces.api.abilities.generic.BaseAbility;
import justjabka.justraces.api.abilities.generic.ResettableAbility;
import justjabka.justraces.api.interfaces.Trait;
import justjabka.justraces.api.modifiers.generic.BaseModifier;
import justjabka.justraces.api.types.TransientContainer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class TransientManager {

    private TransientManager() {}

    private static final Map<UUID, TransientContainer> TRANSIENT_CONTAINER = new ConcurrentHashMap<>();

    public static TransientContainer getTransientContainer(Player player) {
        return TRANSIENT_CONTAINER.getOrDefault(player.getUniqueId(), TransientContainer.ofDefault());
    }

    @NotNull
    public static Set<@NotNull BaseAbility> getTransientAbilities(Player player) {
        Map<BaseAbility, Long> transientAbilities = TransientManager.getTransientContainer(player).abilities();

        transientAbilities.entrySet().removeIf(entry -> {
            BaseAbility ability = entry.getKey();
            Long stamp = entry.getValue();

            boolean expired = !TimeManager.isExpireStampValid(stamp);
            if (expired) {
                AbilityManager.endAbility(player, ability, ResettableAbility.Reason.ABILITY_END);
            }
            return expired;
        });

        return transientAbilities.keySet();
    }

    @NotNull
    public static Set<@NotNull Trait> getTransientTraits(Player player) {
        Map<Trait, Long> transientTraits = TransientManager.getTransientContainer(player).traits();

        transientTraits.entrySet().removeIf(entry -> {
            Trait trait = entry.getKey();
            Long stamp = entry.getValue();

            boolean expired = !TimeManager.isExpireStampValid(stamp);
            if (expired) {
                // TODO: add resettable trait
            }
            return expired;
        });

        return transientTraits.keySet();
    }

//    @NotNull
//    public static Map<BaseModifier, Set<Material>> getTransientItemModifiers(Player player) {
//
//    }

    // TODO: refactor
    public static void addTransientAbility(Player player, BaseAbility ability, long ticks) {
        TransientContainer container = getTransientContainer(player);
        long expireStamp = TimeManager.getExpireStamp(ticks);

        Map<BaseAbility, Long> newAbilities = new ConcurrentHashMap<>(Map.copyOf(container.abilities()));
        newAbilities.put(ability, expireStamp);

        TRANSIENT_CONTAINER.put(player.getUniqueId(), new TransientContainer(
                newAbilities,
                container.traits(),
                container.itemModifiers()
        ));
    }

    public static void addTransientTrait(Player player, Trait trait, long ticks) {
        TransientContainer container = getTransientContainer(player);
        long expireStamp = TimeManager.getExpireStamp(ticks);

        Map<Trait, Long> newTraits = new ConcurrentHashMap<>(Map.copyOf(container.traits()));
        newTraits.put(trait, expireStamp);

        TRANSIENT_CONTAINER.put(player.getUniqueId(), new TransientContainer(
                container.abilities(),
                newTraits,
                container.itemModifiers()
        ));
    }

//    public static void addTransientItemModifier(Player player, Map<BaseModifier, Set<Material>> modifier, long ticks) {
//        TransientContainer container = getTransientContainer(player);
//        long expireStamp = TimeManager.getExpireStamp(ticks);
//
//        Map<Map<BaseModifier, Set<Material>>, Long> newItemModifiers = new ConcurrentHashMap<>(Map.copyOf(container.itemModifiers()));
//        newItemModifiers.put(modifier, expireStamp);
//
//        TRANSIENT_CONTAINER.put(player.getUniqueId(), new TransientContainer(
//                container.abilities(),
//                container.traits(),
//                newItemModifiers
//        ));
//    }
}
