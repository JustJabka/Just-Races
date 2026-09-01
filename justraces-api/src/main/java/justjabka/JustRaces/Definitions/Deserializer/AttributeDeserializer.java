package justjabka.JustRaces.Definitions.Deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;

import java.lang.reflect.Type;

public class AttributeDeserializer implements JsonDeserializer<Attribute> {

    @Override
    public Attribute deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonPrimitive() || !json.getAsJsonPrimitive().isString()) {
            throw new JsonParseException("Expected an attribute Key string, but got: %s".formatted(json));
        }

        String keyStr = json.getAsString();
        NamespacedKey key = NamespacedKey.fromString(keyStr);

        if (key == null) {
            throw new JsonParseException("Invalid NamespacedKey format: %s".formatted(keyStr));
        }

        Attribute attribute = Registry.ATTRIBUTE.get(key);
        if (attribute == null) {
            throw new JsonParseException("Unknown attribute: %s".formatted(keyStr));
        }

        return attribute;
    }
}
