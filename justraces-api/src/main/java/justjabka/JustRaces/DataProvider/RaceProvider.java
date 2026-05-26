package justjabka.JustRaces.DataProvider;

import justjabka.JustRaces.Instances.RaceInstance;
import justjabka.JustRaces.JustRacesAPI;
import justjabka.JustRaces.Registries.RacesRegistry;
import net.kyori.adventure.key.KeyPattern;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public class RaceProvider {
    public static final NamespacedKey ARMAT = create("armat");
    public static final NamespacedKey EPIPHYTE = create("epiphyte");
    public static final NamespacedKey FETR = create("fetr");
    public static final NamespacedKey HUMAN = create("human");
    public static final NamespacedKey LIZARD = create("lizard");
    public static final NamespacedKey PHANTOM = create("phantom");
    public static final NamespacedKey SKYZERN = create("skyzern");

    @NotNull
    private static NamespacedKey create(@NotNull @KeyPattern.Value String key) {
        return new NamespacedKey(JustRacesAPI.NAMESPACE, key);
    }

    public static RaceInstance get(NamespacedKey key) {
        return RacesRegistry.getRaces().get(key);
    }
}
