package justjabka.JustRaces.Gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import justjabka.JustRaces.Gson.Deserializer.*;
import justjabka.JustRaces.Interfaces.Trait;
import justjabka.JustRaces.Modifiers.Generic.BaseModifier;
import justjabka.JustRaces.Types.AbilityBinding;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;

import java.lang.reflect.Type;
import java.util.Set;

public final class GsonManager {
    private static final Type SET_MATERIAL = new TypeToken<Set<Material>>() {}.getType();

    public static final Gson GSON = GsonComponentSerializer.gson().populator()
            .apply(new GsonBuilder())
            .registerTypeAdapter(AbilityBinding.class, new AbilityBindingDeserializer())
            .registerTypeAdapter(Attribute.class, new AttributeDeserializer())
            .registerTypeAdapter(BaseModifier.class, new ItemModifierDeserializer())
            .registerTypeAdapter(Trait.class, new TraitDeserializer())
            .registerTypeAdapter(SET_MATERIAL, new ItemsDeserializer())
            .create();

    private GsonManager() {}
}
