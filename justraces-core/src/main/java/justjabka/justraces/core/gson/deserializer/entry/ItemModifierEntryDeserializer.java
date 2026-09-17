package justjabka.justraces.core.gson.deserializer.entry;

import com.google.gson.*;
import justjabka.justraces.api.JustRacesAPI;
import justjabka.justraces.api.common.entry.ItemModifierEntry;
import justjabka.justraces.api.itemmodifiers.generic.BaseItemModifier;
import justjabka.justraces.api.managers.ItemModifierManager;
import org.bukkit.*;
import org.jspecify.annotations.NonNull;

import java.lang.reflect.Type;
import java.util.*;

public class ItemModifierEntryDeserializer implements JsonDeserializer<Set<ItemModifierEntry>> {

    @Override
    public Set<ItemModifierEntry> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonObject()) {
            throw new JsonParseException("Expected a JsonObject for Item Modifiers map, but got: " + json);
        }

        JsonObject obj = json.getAsJsonObject();
        Set<ItemModifierEntry> entries = new HashSet<>();

        for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
            BaseItemModifier modifier = parseModifier(entry.getKey());
            Set<Material> materials = parseMaterials(entry.getValue());

            entries.add(new ItemModifierEntry(modifier, materials));
        }

        return entries;
    }

    @NonNull
    private static BaseItemModifier parseModifier(String keyStr) {
        NamespacedKey key = NamespacedKey.fromString(keyStr);

        if (key == null) {
            throw new JsonParseException("Invalid NamespacedKey format for Item Modifier: " + keyStr);
        }

        BaseItemModifier modifier = ItemModifierManager.getByKey(key);
        if (modifier == null) {
            throw new JsonParseException("Unknown Item Modifier key: " + keyStr);
        }

        return modifier;
    }

    @NonNull
    private Set<Material> parseMaterials(JsonElement element) {
        Set<Material> materials = new HashSet<>();

        if (element == null || element.isJsonNull()) {
            return materials;
        }

        if (isString(element)) {
            parseAndAddMaterialOrTag(element.getAsString(), materials);
        } else if (element.isJsonArray()) {
            for (JsonElement itemElement : element.getAsJsonArray()) {
                if (!isString(itemElement)) continue;
                parseAndAddMaterialOrTag(itemElement.getAsString(), materials);
            }
        }

        return materials;
    }

    private void parseAndAddMaterialOrTag(String value, Set<Material> materials) {
        if (value.startsWith("#")) {
            String tagKeyString = value.substring(1); // Remove #
            NamespacedKey tagKey = NamespacedKey.fromString(tagKeyString);

            if (tagKey == null) return;

            Tag<Material> itemTag = Bukkit.getTag(Tag.REGISTRY_ITEMS, tagKey, Material.class);

            if (itemTag != null) {
                materials.addAll(itemTag.getValues());
                return;
            }

            JustRacesAPI.getLogger().warn("Unknown Item Tag in JSON: {}", value);
        } else {
            NamespacedKey materialKey = NamespacedKey.fromString(value);
            if (materialKey == null) return;

            Material material = Registry.MATERIAL.get(materialKey);
            if (material == null) return;

            materials.add(material);
        }
    }

    private static boolean isString(JsonElement element) {
        return element.isJsonPrimitive() && element.getAsJsonPrimitive().isString();
    }
}
