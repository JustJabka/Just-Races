package justjabka.justraces.core.gson.deserializer;

import com.google.gson.JsonParseException;
import org.jetbrains.annotations.Nullable;

public final class DeserializationExceptions {

    private DeserializationExceptions() {}

    public static JsonParseException invalidKeyFormat(String id) {
        return new JsonParseException("Invalid NamespacedKey format: %s".formatted(id));
    }

    public static JsonParseException unknownKey(String category, String id) {
        return unknownKey(category, null, id);
    }

    public static JsonParseException unknownKey(String category, @Nullable String location, String id) {
        if (location == null) return new JsonParseException("Unknown %s %s".formatted(category, id));
        return new JsonParseException("Unknown %s in %s %s".formatted(category, location, id));
    }

    public static JsonParseException missingOrInvalidField(String field, String location) {
        return new JsonParseException("Missing or invalid '%s' field in %s".formatted(field, location));
    }
}
