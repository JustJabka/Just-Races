package justjabka.justraces.api.managers;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ComponentManager {

    private ComponentManager() {}

    public static String fallbackFromKey(NamespacedKey key) {
        final String name = key.getKey();
        final String fallback = StringUtils.replace(name, "_", " ");
        return WordUtils.capitalizeFully(fallback);
    }
}
