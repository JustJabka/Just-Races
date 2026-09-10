package justjabka.justraces.showcase.dataprovider;

import justjabka.justraces.showcase.JustRacesShowcase;
import net.kyori.adventure.key.KeyPattern;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public class RaceProvider {
    public static final NamespacedKey HUMAN = create("human");
    public static final NamespacedKey ARMAT = create("armat");
    public static final NamespacedKey FROGGISH = create("froggish");
    public static final NamespacedKey SKYZERN = create("skyzern");
    public static final NamespacedKey BUZZLING = create("buzzling");
    public static final NamespacedKey PROWLER = create("prowler");

    @NotNull
    private static NamespacedKey create(@NotNull @KeyPattern.Value String key) {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, key);
    }
}
