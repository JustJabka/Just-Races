package justjabka.JustRaces.Instances;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import justjabka.JustRaces.Instances.Generic.BaseInstance;
import justjabka.JustRaces.JustRacesAPI;
import justjabka.JustRaces.JustRacesRegistries;
import justjabka.JustRaces.Modifiers.ItemModifier;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;

import java.util.*;

public class RaceInstance extends BaseInstance {
    private JsonElement name;
    private List<JsonObject> description;
    private Map<String, Double> attributes;
    private Set<String> abilities;
    private Map<String, JsonElement> item_modifiers;
    private transient Map<Material, ItemModifier> cachedModifiers = new HashMap<>();
    private transient boolean isModifiersCacheBuilt = false;

    public Component getName() {
        if (name == null || name.isJsonNull()) {
            return Component.empty();
        }

        try {
            return GsonComponentSerializer.gson().deserializeFromTree(name);
        } catch (Exception e) {
            JustRacesAPI.getLogger().error("Failed to parse name for race: {}. Returning key instead of name", getKey(), e);
            return Component.text(getKey().getKey());
        }
    }

    public List<Component> getDescription() {
        List<Component> contents = new ArrayList<>();

        if (description == null || description.isEmpty()) {
            contents.add(Component.empty());
            return contents;
        }

        for (JsonObject object : description) {
            try {
                Component line = GsonComponentSerializer.gson().deserializeFromTree(object);
                contents.add(line);
            } catch (Exception e) {
                JustRacesAPI.getLogger().error("Failed to parse description for race: {}", getKey(), e);
            }
        }

        return contents;
    }

    public Map<Attribute, Double> getAttributes() {
        Map<Attribute, Double> bukkitAttributes = new HashMap<>();
        if (attributes == null) return bukkitAttributes;

        for (Map.Entry<String, Double> entry : attributes.entrySet()) {
            NamespacedKey key = NamespacedKey.fromString(entry.getKey());
            if (key == null) continue;

            Attribute attribute = Registry.ATTRIBUTE.get(key);
            if (attribute == null) continue;

            bukkitAttributes.put(attribute, entry.getValue());
        }
        return bukkitAttributes;
    }

    public Set<String> getAbilities() {
        return abilities;
    }

    public ItemModifier getModifier(Material material) {
        if (!this.isModifiersCacheBuilt) buildModifierCache();

        return this.cachedModifiers.get(material);
    }

    private void buildModifierCache() {
        this.cachedModifiers.clear();
        this.isModifiersCacheBuilt = true;

        if (this.item_modifiers == null) return;
        if (this.item_modifiers.isEmpty()) return;

        for (NamespacedKey modifierKey : JustRacesRegistries.MODIFIERS.keys()) {
            Set<Material> materials = getMaterialsForModifier(modifierKey.toString());

            if (materials.isEmpty()) {
                materials = getMaterialsForModifier(modifierKey.getKey());
            }

            if (materials.isEmpty()) continue;

            ItemModifier modifier = JustRacesRegistries.MODIFIERS.get(modifierKey);

            for (Material material : materials) {
                this.cachedModifiers.put(material, modifier);
            }
        }
    }

    public Set<Material> getMaterialsForModifier(String modifierId) {
        Set<Material> materials = new HashSet<>();

        if (item_modifiers == null) return materials;
        if (!item_modifiers.containsKey(modifierId)) return materials;

        JsonElement element = item_modifiers.get(modifierId);

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

    private boolean isString(JsonElement element) {
        return element.isJsonPrimitive() && element.getAsJsonPrimitive().isString();
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

            Material material = org.bukkit.Registry.MATERIAL.get(materialKey);
            if (material == null) return;

            materials.add(material);
        }
    }
}