package justjabka.JustRaces.Gson.Deserializer;

import justjabka.JustRaces.Interfaces.Trait;
import justjabka.JustRaces.Managers.TraitManager;
import org.bukkit.NamespacedKey;

public class TraitDeserializer implements NamespacedKeyDeserializer<Trait> {

    @Override
    public Trait getByKey(NamespacedKey key) {
        return TraitManager.getByKey(key);
    }
}
