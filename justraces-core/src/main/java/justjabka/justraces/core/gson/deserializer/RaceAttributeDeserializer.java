package justjabka.justraces.core.gson.deserializer;

import com.google.gson.*;
import justjabka.justraces.api.types.race.RaceAttribute;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

import java.lang.reflect.Type;
import java.util.Optional;

public class RaceAttributeDeserializer implements JsonDeserializer<RaceAttribute> {

    @Override
    public RaceAttribute deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonObject()) return null;

        JsonObject obj = json.getAsJsonObject();

        Attribute attribute =  getAttribute(obj);
        double amount = getAmount(obj);
        Optional<AttributeModifier.Operation> operation = getOperation(obj);

        return new RaceAttribute(attribute, amount, operation);
    }

    private static Attribute getAttribute(JsonObject obj) {
        String idStr = obj.get("id").getAsString();
        NamespacedKey id = NamespacedKey.fromString(idStr);

        if (id == null) {
            throw new JsonParseException("Invalid NamespacedKey format: %s".formatted(idStr));
        }

        return Registry.ATTRIBUTE.getOrThrow(id);
    }

    private static double getAmount(JsonObject obj) {
        return obj.get("amount").getAsDouble();
    }

    private static Optional<AttributeModifier.Operation> getOperation(JsonObject obj) {
        if (!obj.has("operation") || obj.get("operation").isJsonNull()) {
            return Optional.empty();
        }

        String opStr = obj.get("operation").getAsString();

        try {
            return Optional.of(AttributeModifier.Operation.valueOf(opStr.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new JsonParseException("Unknown attribute operation: '%s'".formatted(opStr));
        }
    }
}
