package justjabka.justraces.api.common.definition;

import justjabka.justraces.api.JustRacesAPI;
import org.bukkit.NamespacedKey;

@SuppressWarnings({"unused", "MismatchedQueryAndUpdateOfCollection"})
public abstract class BaseDefinition {
    private transient String key;

    public NamespacedKey getKey() {
        return NamespacedKey.fromString(key, JustRacesAPI.getInstance());
    }

    public void setKey(String key) {
        this.key = key;
    }
}
