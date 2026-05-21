package justjabka.WeltenRaces.Instances.Generic;

import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.NamespacedKey;

public abstract class BaseInstance {
    private transient String key;

    public NamespacedKey getKey() {
        return NamespacedKey.fromString(key, WeltenRaces.INSTANCE);
    }

    public void setKey(String key) {
        this.key = key;
    }
}
