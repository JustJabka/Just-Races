package justjabka.justraces.showcase.dataprovider;

import justjabka.justraces.showcase.JustRacesShowcase;
import net.kyori.adventure.key.KeyPattern;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NonNull;

public class RaceProvider {
    public static final NamespacedKey HUMAN = create("human");
    public static final NamespacedKey ARMAT = create("armat");
    public static final NamespacedKey FROGGISH = create("froggish");
    public static final NamespacedKey SKYZERN = create("skyzern");
    public static final NamespacedKey BUZZLING = create("buzzling");
    public static final NamespacedKey PROWLER = create("prowler");

    @NonNull
    private static NamespacedKey create(@NonNull @KeyPattern.Value String key) {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, key);
    }
}
