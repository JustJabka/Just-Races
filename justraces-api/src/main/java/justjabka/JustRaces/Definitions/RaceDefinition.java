package justjabka.JustRaces.Definitions;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import justjabka.JustRaces.Abilities.Generic.BaseAbility;
import justjabka.JustRaces.Definitions.Generic.BaseDefinition;
import justjabka.JustRaces.JustRacesAPI;
import justjabka.JustRaces.Modifiers.Generic.BaseModifier;
import justjabka.JustRaces.Types.AbilityBinding;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Tag;
import org.bukkit.attribute.Attribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class RaceDefinition extends BaseDefinition {
    private Component name;
    private List<Component> description;
    private Map<Attribute, Double> attributes;
    private Set<AbilityBinding> abilities;

    @SerializedName("item_modifiers")
    private Map<BaseModifier, JsonElement> itemModifiers;
    private Boolean hidden;

    private transient Set<BaseAbility> cachedAbilities;
    private transient Map<Material, BaseModifier> cachedModifiers;

    // Getters
    public Component getName() {
        return name != null ? name : Component.empty();
    }

    @NotNull
    public List<@NotNull Component> getDescription() {
        return description != null ? Collections.unmodifiableList(description) : Collections.emptyList();
    }

    public boolean isHidden() {
        return Boolean.TRUE.equals(hidden);
    }

    @NotNull
    public Map<@NotNull Attribute, @NotNull Double> getAttributes() {
        return attributes != null ? Collections.unmodifiableMap(attributes) : Collections.emptyMap();
    }

    @NotNull
    public Set<@NotNull BaseAbility> getAbilities() {
        if (cachedAbilities == null) buildAbilitiesCache();
        return cachedAbilities;
    }

    @NotNull
    public Set<@NotNull AbilityBinding> getAbilitiesBindings() {
        return abilities != null ? Collections.unmodifiableSet(abilities) : Collections.emptySet();
    }

    @Nullable
    public BaseModifier getModifier(Material material) {
        if (cachedModifiers == null) buildModifiersCache();
        return cachedModifiers.get(material);
    }

    // Cache
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

        for (Map.Entry<BaseModifier, JsonElement> entry : itemModifiers.entrySet()) {
            BaseModifier modifier = entry.getKey();
            JsonElement element = entry.getValue();

            Set<Material> materials = parseMaterialsFromElement(element);
            for (Material material : materials) {
                cachedModifiers.put(material, modifier);
            }
        }
    }

    private Set<Material> parseMaterialsFromElement(JsonElement element) {
        Set<Material> materials = new HashSet<>();
        if (element == null) return materials;

        if (isString(element)) {
            parseAndAddMaterialOrTag(element.getAsString(), materials);
        } else if (element.isJsonArray()) {
            for (JsonElement arrayElement : element.getAsJsonArray()) {
                if (!isString(arrayElement)) continue;
                parseAndAddMaterialOrTag(arrayElement.getAsString(), materials);
            }
        }

        return materials;
    }

    private void parseAndAddMaterialOrTag(String value, Set<Material> materials) {
        boolean isTag = value.startsWith("#");

        if (isTag) {
            String tagKeyString = value.substring(1); // Remove tag prefix
            NamespacedKey tagKey = NamespacedKey.fromString(tagKeyString);

            if (tagKey == null) return;

            Tag<Material> itemTag = Bukkit.getTag(Tag.REGISTRY_ITEMS, tagKey, Material.class);

            if (itemTag != null) {
                materials.addAll(itemTag.getValues());
                return;
            }

            JustRacesAPI.getLogger().warn("Unknown item tag in JSON: {}", value);
        } else {
            NamespacedKey materialKey = NamespacedKey.fromString(value);
            if (materialKey == null) return;

            Material material = Registry.MATERIAL.get(materialKey);
            if (material == null) return;

            materials.add(material);
        }
    }
}