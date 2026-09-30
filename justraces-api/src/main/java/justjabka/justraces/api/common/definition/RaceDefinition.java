package justjabka.justraces.api.common.definition;

import com.google.gson.annotations.SerializedName;
import justjabka.justraces.api.abilities.generic.BaseAbility;
import justjabka.justraces.api.common.Displayable;
import justjabka.justraces.api.common.entry.*;
import justjabka.justraces.api.itemmodifiers.generic.BaseItemModifier;
import justjabka.justraces.api.traits.generic.Trait;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.CraftingRecipe;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@NullMarked
@SuppressWarnings({"unused", "MismatchedQueryAndUpdateOfCollection"})
public class RaceDefinition extends BaseDefinition implements Displayable {
    @Nullable private Component name;
    @Nullable private List<Component> description;
    @Nullable private Component icon;
    @Nullable private List<AttributeEntry> attributes;
    @Nullable private Set<AbilityEntry> abilities;
    @Nullable private Set<Trait> traits;

    @SerializedName("item_modifiers")
    @Nullable private Set<ItemModifierEntry> itemModifiers;

    @Nullable private Set<CraftingRecipe> recipes;
    @Nullable private Boolean hidden;

    @Nullable private transient CachedAbilities cachedAbilities;
    @Nullable private transient CachedItemModifiers cachedItemModifiers;
    @Nullable private transient CachedRecipes cachedRecipes;

    /**
     * @return Name of the race
     */
    @Override
    public Component name() {
        return name != null ? name : Component.empty();
    }

    /**
     * @return Description of the race
     */
    @Override
    public List<Component> description() {
        return description != null ? Collections.unmodifiableList(description) : Collections.emptyList();
    }

    /**
     * @return Icon of the race
     */
    public Component icon() {
        return icon != null ? icon : Component.empty();
    }

    /**
     * @return Base Attribute Entries of the race
     */
    public List<AttributeEntry> attributeEntries() {
        return attributes != null ? Collections.unmodifiableList(attributes) : Collections.emptyList();
    }

    /**
     * @return Abilities of the race
     * @see BaseAbility
     */
    public Set<BaseAbility> abilities() {
        if (cachedAbilities == null) {
            cachedAbilities = CachedAbilities.buildCache(abilities);
        }

        return cachedAbilities.abilities();
    }

    /**
     * @return Ability Entries of the race
     * @see AbilityEntry
     */
    public Set<AbilityEntry> abilityEntries() {
        return abilities != null ? Collections.unmodifiableSet(abilities) : Collections.emptySet();
    }

    /**
     * @return Traits of the race
     * @see Trait
     */
    public Set<Trait> traits() {
        return traits != null ? Collections.unmodifiableSet(traits) : Collections.emptySet();
    }

    /**
     * @return Item Modifier Entries of the race
     * @see ItemModifierEntry
     */
    public Set<ItemModifierEntry> itemModifiers() {
        return itemModifiers != null ? Collections.unmodifiableSet(itemModifiers) : Collections.emptySet();
    }

    /**
     * Gets modifier assigned for the material
     * @param material Material
     * @return Modifier assigned for the material
     * @see BaseItemModifier
     */
    public @Nullable BaseItemModifier getItemModifierForMaterial(@Nullable Material material) {
        if (material == null) return null;

        if (cachedItemModifiers == null) {
            cachedItemModifiers = CachedItemModifiers.buildCache(itemModifiers);
        }

        return cachedItemModifiers.modifiers().get(material);
    }

    /**
     * @return Recipes of the race
     * @see CraftingRecipe
     */
    public Set<CraftingRecipe> recipes() {
        return recipes != null ? Collections.unmodifiableSet(recipes) : Collections.emptySet();
    }

    /**
     * Checks if the race has this recipe
     * @param recipe Recipe
     * @return {@code true} if the race has this recipe
     */
    public boolean hasRecipe(CraftingRecipe recipe) {
        if (cachedRecipes == null) {
            cachedRecipes = CachedRecipes.buildCache(recipes());
        }

        return cachedRecipes.recipes().containsKey(recipe.getKey());
    }

    /**
     * @return {@code true} if race is hidden from the race selection dialog ({@code /selectrace})
     */
    public boolean isHidden() {
        return hidden != null && hidden;
    }
}