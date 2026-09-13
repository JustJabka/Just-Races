package justjabka.justraces.core.gson.deserializer;

import justjabka.justraces.api.traits.generic.Trait;
import justjabka.justraces.api.managers.TraitManager;
import org.bukkit.NamespacedKey;

public class TraitDeserializer implements NamespacedKeyDeserializer<Trait> {

    @Override
    public Trait getByKey(NamespacedKey key) {
        return TraitManager.getByKey(key);
    }
}
