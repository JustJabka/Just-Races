package justjabka.justraces.api.managers;

import net.kyori.adventure.key.Key;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ComponentManager {

    private ComponentManager() {}

    public static String fallbackFromKey(Key key) {
        final String name = key.value();
        final String fallback = StringUtils.replace(name, "_", " ");
        return WordUtils.capitalizeFully(fallback);
    }
}
