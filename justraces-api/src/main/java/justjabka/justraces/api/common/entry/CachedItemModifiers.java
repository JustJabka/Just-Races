package justjabka.justraces.api.common.entry;

import justjabka.justraces.api.itemmodifiers.generic.BaseItemModifier;
import org.bukkit.Material;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public record CachedItemModifiers(Map<Material, BaseItemModifier> modifiers, Set<ItemModifierEntry> entries) {

    public static CachedItemModifiers ofEmpty() {
        return new CachedItemModifiers(Collections.emptyMap(), Collections.emptySet());
    }

    public static CachedItemModifiers buildCache(Set<ItemModifierEntry> entries) {
        if (entries == null || entries.isEmpty()) return new CachedItemModifiers(
                Collections.emptyMap(),
                Collections.emptySet()
        );

        final Map<Material, BaseItemModifier> cachedModifiers = new HashMap<>();

        for (ItemModifierEntry entry : entries) {
            BaseItemModifier modifier = entry.modifier();
            if (modifier == null) continue;

            Set<Material> materials = entry.materials();
            if (materials == null) continue;

            for (Material material : materials) {
                cachedModifiers.put(material, modifier);
            }
        }

        return new CachedItemModifiers(
                Collections.unmodifiableMap(cachedModifiers),
                Collections.unmodifiableSet(entries)
        );
    }
}
