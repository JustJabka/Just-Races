package justjabka.justraces.api.common.definition;

import justjabka.justraces.api.JustRacesAPI;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NonNull;

@SuppressWarnings({"unused", "MismatchedQueryAndUpdateOfCollection"})
public abstract class BaseDefinition implements Keyed {
    private transient String key;

    public @NonNull NamespacedKey getKey() {
        return NamespacedKey.fromString(key, JustRacesAPI.getInstance());
    }

    public void setKey(String key) {
        this.key = key;
    }
}
