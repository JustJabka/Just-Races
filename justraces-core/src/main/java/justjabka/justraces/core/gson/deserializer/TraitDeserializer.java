package justjabka.justraces.core.gson.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import justjabka.justraces.api.traits.generic.Trait;
import justjabka.justraces.api.managers.TraitManager;
import org.bukkit.NamespacedKey;

import java.lang.reflect.Type;

public class TraitDeserializer implements JsonDeserializer<Trait> {

    @Override
    public Trait deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonPrimitive() || !json.getAsJsonPrimitive().isString()) {
            throw new JsonParseException("Expected a Key string, but got: %s".formatted(json));
        }

        String keyStr = json.getAsString();
        NamespacedKey key = NamespacedKey.fromString(keyStr);

        if (key == null) {
            throw DeserializationExceptions.invalidKeyFormat(keyStr);
        }

        try {
            return TraitManager.getByKey(key);
        } catch (IllegalArgumentException _) {
            throw DeserializationExceptions.unknownKey("Trait", keyStr);
        }
    }
}
