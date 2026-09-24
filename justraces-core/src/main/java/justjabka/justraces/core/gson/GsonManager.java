package justjabka.justraces.core.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import justjabka.justraces.api.common.entry.AbilityEntry;
import justjabka.justraces.api.common.entry.AttributeEntry;
import justjabka.justraces.api.common.entry.ItemModifierEntry;
import justjabka.justraces.api.traits.generic.Trait;
import justjabka.justraces.core.gson.deserializer.TraitDeserializer;
import justjabka.justraces.core.gson.deserializer.entry.AbilityEntryDeserializer;
import justjabka.justraces.core.gson.deserializer.entry.AttributeEntryDeserializer;
import justjabka.justraces.core.gson.deserializer.entry.ItemModifierEntryDeserializer;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.lang.reflect.Type;
import java.util.Set;

public final class GsonManager {

    private GsonManager() {}

    private static final Type SET_ITEM_MODIFIER_ENTRIES = new TypeToken<Set<ItemModifierEntry>>(){}.getType();

    public static final Gson GSON = GsonComponentSerializer.gson().populator()
            .apply(new GsonBuilder())
            .registerTypeAdapter(AbilityEntry.class, new AbilityEntryDeserializer())
            .registerTypeAdapter(AttributeEntry.class, new AttributeEntryDeserializer())
            .registerTypeAdapter(SET_ITEM_MODIFIER_ENTRIES, new ItemModifierEntryDeserializer())
            .registerTypeAdapter(Trait.class, new TraitDeserializer())
            .create();
}
