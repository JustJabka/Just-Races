package justjabka.JustRaces.Definitions.Deserializer;

import com.google.gson.*;
import justjabka.JustRaces.Managers.ModifierManager;
import justjabka.JustRaces.Modifiers.Generic.BaseModifier;
import org.bukkit.NamespacedKey;

import java.lang.reflect.Type;

public class ItemModifierDeserializer implements JsonDeserializer<BaseModifier> {

    @Override
    public BaseModifier deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonPrimitive() || !json.getAsJsonPrimitive().isString()) {
            throw new JsonParseException("Expected a modifier Key string, but got: %s".formatted(json));
        }

        String keyStr = json.getAsString();
        NamespacedKey key = NamespacedKey.fromString(keyStr);

        if (key == null) {
            throw new JsonParseException("Invalid NamespacedKey format: %s".formatted(keyStr));
        }

        BaseModifier modifier = ModifierManager.getByKey(key);
        if (modifier == null) {
            throw new JsonParseException("Unknown item modifier: %s".formatted(keyStr));
        }

        return modifier;
    }
}
