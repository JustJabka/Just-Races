package justjabka.justraces.api.common.definition;

import com.google.gson.annotations.SerializedName;
import justjabka.justraces.api.abilities.generic.BaseAbility;
import justjabka.justraces.api.common.entry.ItemModifierEntry;
import justjabka.justraces.api.traits.generic.Trait;
import justjabka.justraces.api.itemmodifiers.generic.BaseItemModifier;
import justjabka.justraces.api.common.entry.AbilityEntry;
import justjabka.justraces.api.common.entry.AttributeEntry;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"unused", "MismatchedQueryAndUpdateOfCollection"})
public class RaceDefinition extends BaseDefinition {
    private Component name;
    private List<Component> description;
    private Component icon;
    private List<AttributeEntry> attributes;
    private Set<AbilityEntry> abilities;
    private Set<Trait> traits;

    @SerializedName("item_modifiers")
    private Set<ItemModifierEntry> itemModifiers;
    private Boolean hidden;

    private transient Set<BaseAbility> cachedAbilities;

    // region Getters

    /**
     * @return Name of the race
     */
    @NotNull
    public Component getName() {
        return name != null ? name : Component.empty();
    }

    /**
     * @return Description of the race
     */
    @NotNull
    public List<@NotNull Component> getDescription() {
        return description != null ? Collections.unmodifiableList(description) : Collections.emptyList();
    }

    /**
     * @return Icon of the race
     */
    @NotNull
    public Component getIcon() {
        return icon != null ? icon : Component.empty();
    }

    /**
     * @return Base Attribute Entries of the race
     */
    @NotNull
    public List<@NotNull AttributeEntry> getAttributeEntries() {
        return attributes != null ? Collections.unmodifiableList(attributes) : Collections.emptyList();
    }

    /**
     * @return Abilities of the race
     * @see BaseAbility
     */
    @NotNull
    public Set<@NotNull BaseAbility> getAbilities() {
        if (cachedAbilities == null) buildAbilitiesCache();
        return cachedAbilities;
    }

    /**
     * @return Ability Entries of the race
     * @see AbilityEntry
     */
    @NotNull
    public Set<@NotNull AbilityEntry> getAbilityEntries() {
        return abilities != null ? Collections.unmodifiableSet(abilities) : Collections.emptySet();
    }

    /**
     * @return Traits of the race
     * @see Trait
     */
    @NotNull
    public Set<@NotNull Trait> getTraits() {
        return traits != null ? Collections.unmodifiableSet(traits) : Collections.emptySet();
    }

    /**
     * Gets modifier assigned for the material
     * @param material Material
     * @return Modifier assigned for the material
     * @see BaseItemModifier
     */
    @Nullable
    public BaseItemModifier getItemModifierForMaterial(Material material) {
        if (itemModifiers == null || itemModifiers.isEmpty() || material == null) return null;

        for (ItemModifierEntry entry : itemModifiers) {
            Set<Material> materials = entry.materials();

            if (!materials.contains(material)) continue;
            return entry.modifier();
        }

        return null;
    }

    /**
     * @return {@code true} if race is hidden from the race selection dialog ({@code /selectrace})
     */
    public boolean isHidden() {
        return hidden != null && hidden;
    }
    // endregion

    // region Cache
    private void buildAbilitiesCache() {
        if (abilities == null || abilities.isEmpty()) {
            cachedAbilities = Collections.emptySet();
            return;
        }

        cachedAbilities = abilities.stream()
                .map(AbilityEntry::ability)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableSet());
    }
    // endregion
}