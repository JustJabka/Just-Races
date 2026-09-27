package justjabka.justraces.api.common.entry;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.CraftingRecipe;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public record CachedRecipes(Map<NamespacedKey, CraftingRecipe> recipes) {

    public static CachedRecipes ofEmpty() {
        return new CachedRecipes(
                Collections.emptyMap()
        );
    }

    public static CachedRecipes buildCache(Set<CraftingRecipe> recipes) {
        if (recipes == null || recipes.isEmpty()) return ofEmpty();

        final Map<NamespacedKey, CraftingRecipe> cachedRecipes = new HashMap<>();

        for (CraftingRecipe recipe : recipes) {
            if (recipe == null) continue;

            NamespacedKey key = recipe.getKey();

            cachedRecipes.put(key, recipe);
        }

        return new CachedRecipes(cachedRecipes);
    }
}
