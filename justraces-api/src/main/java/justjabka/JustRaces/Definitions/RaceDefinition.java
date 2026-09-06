package justjabka.JustRaces.Definitions;

import com.google.gson.annotations.SerializedName;
import justjabka.JustRaces.Abilities.Generic.BaseAbility;
import justjabka.JustRaces.Definitions.Generic.BaseDefinition;
import justjabka.JustRaces.Interfaces.Trait;
import justjabka.JustRaces.Modifiers.Generic.BaseModifier;
import justjabka.JustRaces.Types.AbilityBinding;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"unused", "MismatchedQueryAndUpdateOfCollection"})
public class RaceDefinition extends BaseDefinition {
    private Component name;
    private List<Component> description;
    private Map<Attribute, Double> attributes;
    private Set<AbilityBinding> abilities;
    private Set<Trait> traits;

    @SerializedName("item_modifiers")
    private Map<BaseModifier, Set<Material>> itemModifiers;
    private Boolean hidden;

    private transient Set<BaseAbility> cachedAbilities;
    private transient Map<Material, BaseModifier> cachedModifiers;

    // region Getters

    /**
     * @return Name of the race
     */
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
     * @return Base Attributes of the race
     */
    @NotNull
    public Map<@NotNull Attribute, @NotNull Double> getAttributes() {
        return attributes != null ? Collections.unmodifiableMap(attributes) : Collections.emptyMap();
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
     * @return Ability Bindings of the race
     * @see AbilityBinding
     */
    @NotNull
    public Set<@NotNull AbilityBinding> getAbilitiesBindings() {
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
     * @see BaseModifier
     */
    @Nullable
    public BaseModifier getModifier(Material material) {
        if (cachedModifiers == null) buildModifiersCache();
        return cachedModifiers.get(material);
    }

    /**
     * @return {@code true} if race is hidden from the race selection dialog ({@code /selectrace})
     */
    public boolean isHidden() {
        return hidden != null && hidden;
    }
    // endregion

    // region Cache
    @Override
    public void clearDefinitionCache() {
        this.cachedAbilities = null;
        this.cachedModifiers = null;
    }

    private void buildAbilitiesCache() {
        if (abilities == null || abilities.isEmpty()) {
            cachedAbilities = Collections.emptySet();
            return;
        }

        cachedAbilities = abilities.stream()
                .map(AbilityBinding::ability)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableSet());
    }

    private void buildModifiersCache() {
        cachedModifiers = new HashMap<>();

        if (itemModifiers == null || itemModifiers.isEmpty()) return;

        for (Map.Entry<BaseModifier, Set<Material>> entry : itemModifiers.entrySet()) {
            BaseModifier modifier = entry.getKey();
            Set<Material> materials = entry.getValue();

            for (Material material : materials) {
                cachedModifiers.put(material, modifier);
            }
        }
    }
    // endregion
}