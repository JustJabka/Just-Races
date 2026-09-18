package justjabka.justraces.api.managers;

import justjabka.justraces.api.JustRacesAPI;
import justjabka.justraces.api.abilities.generic.BaseAbility;
import justjabka.justraces.api.abilities.generic.ResettableAbility;
import justjabka.justraces.api.common.entry.AbilityEntry;
import justjabka.justraces.api.common.entry.CachedAbilities;
import justjabka.justraces.api.common.entry.CachedItemModifiers;
import justjabka.justraces.api.common.entry.ItemModifierEntry;
import justjabka.justraces.api.traits.generic.Trait;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

// TODO: Rewrite and add separate interface for transient entities
public final class TransientManager {

    private TransientManager() {}

    private static final Map<UUID, TransientContainer> TRANSIENT_CONTAINER = new ConcurrentHashMap<>();

    public static TransientContainer getTransientContainer(Player player) {
        return TRANSIENT_CONTAINER.computeIfAbsent(
                player.getUniqueId(),
                _ -> TransientContainer.ofDefault()
        );
    }

    public static void resetTransientContainer(Player player) {
        TRANSIENT_CONTAINER.remove(player.getUniqueId());
    }

    @NotNull
    public static CachedAbilities getTransientAbilities(Player player) {
        TransientContainer container = TransientManager.getTransientContainer(player);
        Map<AbilityEntry, Long> transientAbilities = container.abilities();

        boolean updated = transientAbilities.entrySet().removeIf(entry -> {
            AbilityEntry ability = entry.getKey();
            Long stamp = entry.getValue();

            boolean expired = !TimeManager.isExpireStampValid(stamp);
            if (expired) {
                AbilityManager.endAbility(player, ability.ability(), ResettableAbility.Reason.ABILITY_END);
            }
            return expired;
        });

        if (updated) {
            rebuildTransientAbilitiesCache(container);
        }

        return container.cachedAbilities().get();
    }

    @NotNull
    public static CachedItemModifiers getTransientItemModifiers(Player player) {
        TransientContainer container = TransientManager.getTransientContainer(player);
        Map<ItemModifierEntry, Long> transientItemModifiers = container.itemModifiers();

        boolean updated = transientItemModifiers.entrySet().removeIf(entry -> {
            Long stamp = entry.getValue();
            return !TimeManager.isExpireStampValid(stamp);
        });

        if (updated) {
            rebuildTransientItemModifiersCache(container);
        }

        return container.cachedItemModifiers().get();
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

    public static void addTransientAbility(Player player, AbilityEntry ability, long ticks) {
        TransientContainer container = getTransientContainer(player);
        long expireStamp = TimeManager.getExpireStamp(ticks);

        container.abilities().put(ability, expireStamp);
        rebuildTransientAbilitiesCache(container);
    }

    public static void addTransientAbility(Player player, BaseAbility ability, long ticks) {
        addTransientAbility(player, AbilityEntry.ofDefault(ability), ticks);
    }

    public static void addTransientTrait(Player player, Trait trait, long ticks) {
        TransientContainer container = getTransientContainer(player);
        long expireStamp = TimeManager.getExpireStamp(ticks);

        container.traits().put(trait, expireStamp);

        TraitManager.startTrait(player, trait);
    }

    public static void addTransientItemModifier(Player player, ItemModifierEntry itemModifier, long ticks) {
        TransientContainer container = getTransientContainer(player);
        long expireStamp = TimeManager.getExpireStamp(ticks);

        container.itemModifiers().put(itemModifier, expireStamp);
        rebuildTransientItemModifiersCache(container);

        ItemModifierManager.refreshModifiers(player);

        // Probably not the best solution, but I'm fucking tired already.
        // Tbh I don't know what I was even thinking when creating this manager.
        UUID pid = player.getUniqueId();
        Bukkit.getScheduler().runTaskLater(JustRacesAPI.getInstance(), () -> {
            Player plr = Bukkit.getPlayer(pid);
            if (plr == null) return;

            ItemModifierManager.refreshModifiers(plr);
        }, ticks + 1L);
    }

    private static void rebuildTransientAbilitiesCache(TransientContainer container) {
        container.cachedAbilities().set(CachedAbilities.buildCache(container.abilities().keySet()));
    }

    private static void rebuildTransientItemModifiersCache(TransientContainer container) {
        container.cachedItemModifiers().set(CachedItemModifiers.buildCache(container.itemModifiers().keySet()));
    }

    public record TransientContainer(
            Map<AbilityEntry, Long> abilities,
            Map<Trait, Long> traits,
            Map<ItemModifierEntry, Long> itemModifiers,

            AtomicReference<CachedAbilities> cachedAbilities,
            AtomicReference<CachedItemModifiers> cachedItemModifiers
    ) {
        public static TransientContainer ofDefault() {
            return new TransientContainer(
                    new ConcurrentHashMap<>(),
                    new ConcurrentHashMap<>(),
                    new ConcurrentHashMap<>(),

                    new AtomicReference<>(CachedAbilities.ofEmpty()),
                    new AtomicReference<>(CachedItemModifiers.ofEmpty())
            );
        }
    }
}
