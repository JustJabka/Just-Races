package justjabka.JustRaces.Definitions.Deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.bukkit.NamespacedKey;

import java.lang.reflect.Type;

public interface NamespacedKeyDeserializer<T> extends JsonDeserializer<T> {

    T getByKey(NamespacedKey key);

    @Override
    default T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonPrimitive() || !json.getAsJsonPrimitive().isString()) {
            throw new JsonParseException("Expected a Key string, but got: %s".formatted(json));
        }

        String keyStr = json.getAsString();
        NamespacedKey key = NamespacedKey.fromString(keyStr);

        if (key == null) {
            throw new JsonParseException("Invalid NamespacedKey format: %s".formatted(keyStr));
        }

        T something = getByKey(key);
        if (something == null) {
            throw new JsonParseException("Unknown: %s".formatted(keyStr));
        }

        return something;
    }
}
