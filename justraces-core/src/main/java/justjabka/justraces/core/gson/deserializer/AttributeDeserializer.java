package justjabka.justraces.core.gson.deserializer;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;

public class AttributeDeserializer implements NamespacedKeyDeserializer<Attribute> {

    @Override
    public Attribute getByKey(NamespacedKey key) {
        return Registry.ATTRIBUTE.get(key);
    }
}
