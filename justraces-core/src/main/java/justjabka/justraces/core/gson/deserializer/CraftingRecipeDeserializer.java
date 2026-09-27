package justjabka.justraces.core.gson.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.Recipe;

import java.lang.reflect.Type;

public class CraftingRecipeDeserializer implements JsonDeserializer<CraftingRecipe> {

    @Override
    public CraftingRecipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonPrimitive() || !json.getAsJsonPrimitive().isString()) {
            throw new JsonParseException("Expected a Key string, but got: %s".formatted(json));
        }

        String keyStr = json.getAsString();
        NamespacedKey key = NamespacedKey.fromString(keyStr);

        if (key == null) {
            throw DeserializationExceptions.invalidKeyFormat(keyStr);
        }


        Recipe recipe = Bukkit.getRecipe(key);

        if (recipe == null) {
            throw DeserializationExceptions.unknownKey("Recipe", keyStr);
        }

        if (!(recipe instanceof CraftingRecipe craftingRecipe) ) {
            throw DeserializationExceptions.unknownKey("Recipe", keyStr);
        }

        return craftingRecipe;
    }
}
