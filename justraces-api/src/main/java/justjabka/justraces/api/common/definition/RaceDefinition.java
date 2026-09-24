package justjabka.justraces.api.common.definition;

import com.google.gson.annotations.SerializedName;
import justjabka.justraces.api.abilities.generic.BaseAbility;
import justjabka.justraces.api.common.entry.*;
import justjabka.justraces.api.itemmodifiers.generic.BaseItemModifier;
import justjabka.justraces.api.traits.generic.Trait;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@NullMarked
@SuppressWarnings({"unused", "MismatchedQueryAndUpdateOfCollection"})
public class RaceDefinition extends BaseDefinition {
    @Nullable private Component name;
    @Nullable private List<Component> description;
    @Nullable private Component icon;
    @Nullable private List<AttributeEntry> attributes;
    @Nullable private Set<AbilityEntry> abilities;
    @Nullable private Set<Trait> traits;

    @SerializedName("item_modifiers")
    @Nullable private Set<ItemModifierEntry> itemModifiers;

    @Nullable private Boolean hidden;

    @Nullable private transient CachedAbilities cachedAbilities;
    @Nullable private transient CachedItemModifiers cachedItemModifiers;

    /**
     * @return Name of the race
     */
    public Component getName() {
        return name != null ? name : Component.empty();
    }

    /**
     * @return Description of the race
     */
    public List<Component> getDescription() {
        return description != null ? Collections.unmodifiableList(description) : Collections.emptyList();
    }

    /**
     * @return Icon of the race
     */
    public Component getIcon() {
        return icon != null ? icon : Component.empty();
    }

    /**
     * @return Base Attribute Entries of the race
     */
    public List<AttributeEntry> getAttributeEntries() {
        return attributes != null ? Collections.unmodifiableList(attributes) : Collections.emptyList();
    }

    /**
     * @return Abilities of the race
     * @see BaseAbility
     */
    public Set<BaseAbility> getAbilities() {
        if (cachedAbilities == null) {
            cachedAbilities = CachedAbilities.buildCache(abilities);
        }

        return cachedAbilities.abilities();
    }

    /**
     * @return Ability Entries of the race
     * @see AbilityEntry
     */
    public Set<AbilityEntry> getAbilityEntries() {
        return abilities != null ? Collections.unmodifiableSet(abilities) : Collections.emptySet();
    }

    /**
     * @return Traits of the race
     * @see Trait
     */
    public Set<Trait> getTraits() {
        return traits != null ? Collections.unmodifiableSet(traits) : Collections.emptySet();
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
     * @return {@code true} if race is hidden from the race selection dialog ({@code /selectrace})
     */
    public boolean isHidden() {
        return hidden != null && hidden;
    }
}