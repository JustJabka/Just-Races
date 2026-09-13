package justjabka.justraces.api.managers;

import justjabka.justraces.api.abilities.generic.BaseAbility;
import justjabka.justraces.api.abilities.generic.ResettableAbility;
import justjabka.justraces.api.traits.generic.Trait;
import justjabka.justraces.api.types.TransientContainer;
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
        return TRANSIENT_CONTAINER.computeIfAbsent(
                player.getUniqueId(),
                _ -> TransientContainer.ofDefault()
        );
    }

    public static void clearTransientContainer(Player player) {
        TRANSIENT_CONTAINER.remove(player.getUniqueId());
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
                TraitManager.endTrait(player, trait);
            }
            return expired;
        });

        return transientTraits.keySet();
    }

    public static void addTransientAbility(Player player, BaseAbility ability, long ticks) {
        TransientContainer container = getTransientContainer(player);
        long expireStamp = TimeManager.getExpireStamp(ticks);

        container.abilities().put(ability, expireStamp);
    }

    public static void addTransientTrait(Player player, Trait trait, long ticks) {
        TransientContainer container = getTransientContainer(player);
        long expireStamp = TimeManager.getExpireStamp(ticks);

        container.traits().put(trait, expireStamp);

        TraitManager.startTrait(player, trait);
    }

    // TODO: add transient item modifier
}
