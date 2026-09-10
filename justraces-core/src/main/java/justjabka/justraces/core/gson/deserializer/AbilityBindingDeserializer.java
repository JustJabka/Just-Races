package justjabka.justraces.core.gson.deserializer;

import com.google.gson.*;
import justjabka.justraces.api.abilities.generic.BaseAbility;
import justjabka.justraces.api.managers.AbilityManager;
import justjabka.justraces.api.types.AbilityBinding;
import justjabka.justraces.api.types.Trigger;
import justjabka.justraces.api.types.TriggerCondition;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NonNull;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class AbilityBindingDeserializer implements JsonDeserializer<AbilityBinding> {

    @Override
    public AbilityBinding deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonPrimitive()) return deserializeDefault(json);
        if (json.isJsonObject()) return deserializeOverride(json);

        throw new JsonParseException("Invalid ability format");
    }

    private static @NonNull AbilityBinding deserializeDefault(JsonElement json) {
        String id = json.getAsString();

        BaseAbility ability = AbilityManager.getByKey(NamespacedKey.fromString(id));
        if (ability == null) {
            throw new JsonParseException("Unknown ability: %s".formatted(id));
        }

        return AbilityBinding.ofDefault(ability);
    }

    private static @NonNull AbilityBinding deserializeOverride(JsonElement json) {
        JsonObject obj = json.getAsJsonObject();

        String id = obj.get("id").getAsString();
        BaseAbility ability = AbilityManager.getByKey(NamespacedKey.fromString(id));
        if (ability == null) {
            throw new JsonParseException("Unknown ability in override: %s".formatted(id));
        }

        Trigger trigger = getTrigger(ability, obj);
        Set<TriggerCondition> conditions = getConditions(ability, obj);

        return new AbilityBinding(ability, trigger, Collections.unmodifiableSet(conditions));
    }

    private static Trigger getTrigger(BaseAbility ability, JsonObject obj) {
        final String triggerField = "trigger";

        Trigger trigger = ability.getDefaultTrigger();
        if (!obj.has(triggerField)) return trigger;

        String triggerStr = obj.get(triggerField).getAsString().toUpperCase();
        trigger = Trigger.valueOf(triggerStr);

        return trigger;
    }

    private static Set<TriggerCondition> getConditions(BaseAbility ability, JsonObject obj) {
        final String conditionsField = "conditions";

        Set<TriggerCondition> conditions = ability.getDefaultTriggerConditions();
        if (!obj.has(conditionsField)) return conditions;

        conditions = new HashSet<>();
        for (JsonElement element : obj.getAsJsonArray(conditionsField)) {
            String conditionStr = element.getAsString().toUpperCase();
            conditions.add(TriggerCondition.valueOf(conditionStr));
        }

        return conditions;
    }
}
