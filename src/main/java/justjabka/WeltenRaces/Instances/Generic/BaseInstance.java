package justjabka.WeltenRaces.Instances.Generic;

import com.google.gson.JsonObject;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.NamespacedKey;

public abstract class BaseInstance {
    private transient String key;
    private JsonObject config;

    public NamespacedKey getKey() {
        return NamespacedKey.fromString(key, WeltenRaces.INSTANCE);
    }
    public void setKey(String key) {
        this.key = key;
    }

    public JsonObject getConfig() {
        return config;
    }
}
