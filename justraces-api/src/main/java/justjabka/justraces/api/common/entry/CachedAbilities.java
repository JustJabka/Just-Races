package justjabka.justraces.api.common.entry;

import justjabka.justraces.api.abilities.generic.BaseAbility;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public record CachedAbilities(Set<BaseAbility> abilities, Set<AbilityEntry> entries) {

    public static CachedAbilities ofEmpty() {
        return new CachedAbilities(Collections.emptySet(), Collections.emptySet());
    }

    public static CachedAbilities buildCache(Set<AbilityEntry> entries) {
        if (entries == null || entries.isEmpty()) return new CachedAbilities(
                Collections.emptySet(),
                Collections.emptySet()
        );

        final Set<BaseAbility> cachedAbilities = new HashSet<>();

        for (AbilityEntry entry : entries) {
            BaseAbility ability = entry.ability();
            if (ability == null) continue;

            cachedAbilities.add(ability);
        }

        return new CachedAbilities(
                Collections.unmodifiableSet(cachedAbilities),
                Collections.unmodifiableSet(entries)
        );
    }
}
