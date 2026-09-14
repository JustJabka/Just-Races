package justjabka.justraces.core.gson.deserializer;

import justjabka.justraces.api.managers.ItemModifierManager;
import justjabka.justraces.api.itemmodifiers.generic.BaseItemModifier;
import org.bukkit.NamespacedKey;

public class ItemModifierDeserializer implements NamespacedKeyDeserializer<BaseItemModifier> {

    @Override
    public BaseItemModifier getByKey(NamespacedKey key) {
        return ItemModifierManager.getByKey(key);
    }
}
