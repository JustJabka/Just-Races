package justjabka.justraces.core.gson.deserializer;

import justjabka.justraces.api.managers.ModifierManager;
import justjabka.justraces.api.modifiers.generic.BaseModifier;
import org.bukkit.NamespacedKey;

public class ItemModifierDeserializer implements NamespacedKeyDeserializer<BaseModifier> {

    @Override
    public BaseModifier getByKey(NamespacedKey key) {
        return ModifierManager.getByKey(key);
    }
}
