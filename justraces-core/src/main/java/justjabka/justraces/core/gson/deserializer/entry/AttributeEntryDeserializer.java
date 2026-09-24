package justjabka.justraces.core.gson.deserializer.entry;

import com.google.gson.*;
import justjabka.justraces.api.common.entry.AttributeEntry;
import justjabka.justraces.core.gson.deserializer.DeserializationExceptions;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

import java.lang.reflect.Type;
import java.util.Optional;

public class AttributeEntryDeserializer implements JsonDeserializer<AttributeEntry> {

    @Override
    public AttributeEntry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonObject()) return null;

        JsonObject obj = json.getAsJsonObject();

        Attribute attribute =  getAttribute(obj);
        double amount = getAmount(obj);
        Optional<AttributeModifier.Operation> operation = getOperation(obj);

        return new AttributeEntry(attribute, amount, operation);
    }

    private static Attribute getAttribute(JsonObject obj) {
        final String idField = "id";

        if (!obj.has(idField) || !obj.get(idField).isJsonPrimitive()) {
            throw DeserializationExceptions.missingOrInvalidField(idField, "Attribute Entry");
        }

        String id = obj.get(idField).getAsString();
        NamespacedKey key = NamespacedKey.fromString(id);

        if (key == null) {
            throw DeserializationExceptions.invalidKeyFormat(id);
        }

        return Registry.ATTRIBUTE.getOrThrow(key);
    }

    private static double getAmount(JsonObject obj) {
        final String amountField = "amount";

        if (!obj.has(amountField) || !obj.get(amountField).isJsonPrimitive()) {
            throw DeserializationExceptions.missingOrInvalidField(amountField, "Attribute Entry");
        }
        return obj.get(amountField).getAsDouble();
    }

    private static Optional<AttributeModifier.Operation> getOperation(JsonObject obj) {
        final String operationField = "operation";

        if (!obj.has(operationField) || obj.get(operationField).isJsonNull()) {
            return Optional.empty();
        }

        String opStr = obj.get(operationField).getAsString();

        try {
            return Optional.of(AttributeModifier.Operation.valueOf(opStr.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new JsonParseException("Unknown attribute operation: '%s'".formatted(opStr));
        }
    }
}
