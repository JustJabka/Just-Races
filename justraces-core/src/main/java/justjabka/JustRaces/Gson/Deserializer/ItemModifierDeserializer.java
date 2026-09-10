package justjabka.JustRaces.Gson.Deserializer;

import justjabka.JustRaces.Managers.ModifierManager;
import justjabka.JustRaces.Modifiers.Generic.BaseModifier;
import org.bukkit.NamespacedKey;

public class ItemModifierDeserializer implements NamespacedKeyDeserializer<BaseModifier> {

    @Override
    public BaseModifier getByKey(NamespacedKey key) {
        return ModifierManager.getByKey(key);
    }
}
