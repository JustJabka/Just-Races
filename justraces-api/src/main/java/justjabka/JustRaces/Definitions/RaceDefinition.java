package justjabka.JustRaces.Definitions;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import justjabka.JustRaces.Abilities.Generic.BaseAbility;
import justjabka.JustRaces.Definitions.Generic.BaseDefinition;
import justjabka.JustRaces.JustRacesAPI;
import justjabka.JustRaces.JustRacesRegistries;
import justjabka.JustRaces.Modifiers.Generic.BaseModifier;
import justjabka.JustRaces.Types.AbilityBinding;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;

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

    private transient Map<Material, BaseModifier> cachedModifiers = new HashMap<>();
    private transient boolean isModifiersCacheBuilt = false;

    public Component getName() {
        return name != null ? name : Component.empty();
    }

    public List<Component> getDescription() {
        return description != null ? description : Collections.emptyList();
    }

    public boolean isHidden() {
        if (hidden == null) {
            return false;
        }

        return hidden;
    }

    public Map<Attribute, Double> getAttributes() {
        return attributes != null ? attributes : Collections.emptyMap();
    }

    public Set<BaseAbility> getAbilities() {
        if (abilities == null || abilities.isEmpty()) {
            return Collections.emptySet();
        }

        return abilities.stream()
                .map(AbilityBinding::ability)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableSet());
    }

    public Set<AbilityBinding> getAbilitiesBindings() {
        return abilities;
    }

    public BaseModifier getModifier(Material material) {
        if (!this.isModifiersCacheBuilt) buildModifierCache();

        return this.cachedModifiers.get(material);
    }

    private void buildModifierCache() {
        this.cachedModifiers.clear();
        this.isModifiersCacheBuilt = true;

        if (this.itemModifiers == null) return;
        if (this.itemModifiers.isEmpty()) return;

        for (BaseModifier modifier : JustRacesRegistries.MODIFIERS.values()) {
            Set<Material> materials = getMaterialsForModifier(modifier);

            if (materials.isEmpty()) {
                materials = getMaterialsForModifier(modifier);
            }

            if (materials.isEmpty()) continue;

            for (Material material : materials) {
                this.cachedModifiers.put(material, modifier);
            }
        }
    }

    private Set<Material> getMaterialsForModifier(BaseModifier modifier) {
        Set<Material> materials = new HashSet<>();

        if (itemModifiers == null) return materials;
        if (!itemModifiers.containsKey(modifier)) return materials;

        JsonElement element = itemModifiers.get(modifier);

        boolean isArray = element.isJsonArray();

        // Pase string and array
        if (isString(element)) {
            String value = element.getAsString();
            parseAndAddMaterialOrTag(value, materials);
        } else if (isArray) {
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