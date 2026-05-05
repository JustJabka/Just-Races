package justjabka.WeltenRaces.DataProvider;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.keys.tags.DamageTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import justjabka.WeltenRaces.WeltenRaces;
import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.bukkit.damage.DamageType;

import java.util.Collection;

@SuppressWarnings("UnstableApiUsage")
public class DamageTypeProvider {
    private static final RegistryAccess registry = RegistryAccess.registryAccess();

    public static final NamespacedKey ABSOLUTE_DAMAGE_KEY = new NamespacedKey(WeltenRaces.NAMESPACE, "absolute_damage");
    public static final TagKey<DamageType> BYPASSES_DODGE_TAG = DamageTypeTagKeys.create(Key.key(WeltenRaces.NAMESPACE, "bypasses_dodge"));
    public static final TagKey<DamageType> BYPASSES_DAMAGE_INVERSION_TAG = DamageTypeTagKeys.create(Key.key(WeltenRaces.NAMESPACE, "bypasses_damage_inversion"));

    public static DamageType getKey(NamespacedKey key) {
        return registry.getRegistry(RegistryKey.DAMAGE_TYPE).getOrThrow(key);
    }

    public static Collection<DamageType> getTagValues(TagKey<DamageType> key) {
        return registry.getRegistry(RegistryKey.DAMAGE_TYPE).getTagValues(key);
    }
}