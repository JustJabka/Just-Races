package justjabka.justraces.core.gson.deserializer.entry;

import com.google.gson.*;
import justjabka.justraces.api.abilities.AbilityTrigger;
import justjabka.justraces.api.abilities.AbilityTriggerCondition;
import justjabka.justraces.api.abilities.generic.BaseAbility;
import justjabka.justraces.api.common.entry.AbilityEntry;
import justjabka.justraces.api.managers.AbilityManager;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NonNull;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class AbilityEntryDeserializer implements JsonDeserializer<AbilityEntry> {

    @Override
    public AbilityEntry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonPrimitive()) return deserializeDefault(json);
        if (json.isJsonObject()) return deserializeOverride(json);

        throw new JsonParseException("Invalid ability format");
    }

    private static @NonNull AbilityEntry deserializeDefault(JsonElement json) {
        String id = json.getAsString();

        NamespacedKey key = NamespacedKey.fromString(id);
        if (key == null) {
            throw new JsonParseException("Invalid NamespacedKey format: %s".formatted(id));
        }

        try {
            BaseAbility ability = AbilityManager.getByKey(key);
            return AbilityEntry.ofDefault(ability);
        } catch (IllegalArgumentException _) {
            throw new JsonParseException("Unknown Ability %s".formatted(id));
        }
    }

    private static @NonNull AbilityEntry deserializeOverride(JsonElement json) {
        JsonObject obj = json.getAsJsonObject();

        if (!obj.has("id") || !obj.get("id").isJsonPrimitive()) {
            throw new JsonParseException("Missing or invalid id field in Ability Entry");
        }

        String id = obj.get("id").getAsString();

        NamespacedKey key = NamespacedKey.fromString(id);
        if (key == null) {
            throw new JsonParseException("Invalid NamespacedKey format: %s".formatted(id));
        }

        try {
            BaseAbility ability = AbilityManager.getByKey(key);
            AbilityTrigger trigger = getTrigger(ability, obj);
            Set<AbilityTriggerCondition> conditions = getConditions(ability, obj);

            return new AbilityEntry(ability, trigger, Collections.unmodifiableSet(conditions));
        } catch (IllegalArgumentException _) {
            throw new JsonParseException("Unknown ability in Ability Entry %s".formatted(id));
        }
    }

    private static AbilityTrigger getTrigger(BaseAbility ability, JsonObject obj) {
        final String triggerField = "trigger";

        AbilityTrigger trigger = ability.getDefaultTrigger();
        if (!obj.has(triggerField)) return trigger;

        String triggerStr = obj.get(triggerField).getAsString().toUpperCase();
        try {
            trigger = AbilityTrigger.valueOf(triggerStr);
            return trigger;
        } catch (IllegalArgumentException e) {
            throw new JsonParseException("Unknown trigger in Ability Entry %s".formatted(triggerStr));
        }
    }

    private static Set<AbilityTriggerCondition> getConditions(BaseAbility ability, JsonObject obj) {
        final String conditionsField = "conditions";

        Set<AbilityTriggerCondition> conditions = ability.getDefaultTriggerConditions();
        if (!obj.has(conditionsField)) return conditions;

        try {
            conditions = new HashSet<>();
            for (JsonElement element : obj.getAsJsonArray(conditionsField)) {
                String conditionStr = element.getAsString().toUpperCase();
                conditions.add(AbilityTriggerCondition.valueOf(conditionStr));
            }

            return conditions;
        } catch (Exception e) {
            throw new JsonParseException("Invalid conditions array in Ability Entry");
        }
    }
}
