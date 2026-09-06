package justjabka.JustRaces.Definitions.Deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import justjabka.JustRaces.JustRacesAPI;
import org.bukkit.*;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class ItemsDeserializer implements JsonDeserializer<Set<Material>> {

    @Override
    public Set<Material> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Set<Material> materials = new HashSet<>();
        if (json == null) return materials;

        if (isString(json)) {
            parseAndAddMaterialOrTag(json.getAsString(), materials);
        } else if (json.isJsonArray()) {
            for (JsonElement element : json.getAsJsonArray()) {
                if (!isString(element)) continue;
                parseAndAddMaterialOrTag(element.getAsString(), materials);
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

    private boolean isString(JsonElement element) {
        return element.isJsonPrimitive() && element.getAsJsonPrimitive().isString();
    }
}
