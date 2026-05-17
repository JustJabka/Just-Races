package justjabka.WeltenRaces.Instances;

import com.google.gson.JsonElement;
import justjabka.WeltenRaces.WeltenRaces;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RaceInstance {
    private transient String key;

    private Map<String, Object> name;
    private Map<String, Double> attributes;
    private Set<String> abilities;
    private Map<String, JsonElement> item_modifiers;

    public void setKey(String key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return NamespacedKey.fromString(key, WeltenRaces.INSTANCE);
    }

    public Component getName() {
        if (name == null) return Component.empty();

        String translate = (String) name.getOrDefault("translate", "");
        String fallback = (String) name.getOrDefault("fallback", "");

        return Component.translatable(translate).fallback(fallback);
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


            WeltenRaces.LOGGER.warn("Unknown item tag in JSON: {}", value);
        } else {
            NamespacedKey materialKey = NamespacedKey.fromString(value);
            if (materialKey == null) return;

            Material material = org.bukkit.Registry.MATERIAL.get(materialKey);
            if (material == null) return;

            materials.add(material);
        }
    }
}