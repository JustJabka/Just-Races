package justjabka.JustRacesShowcase.DataProvider;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.keys.tags.DamageTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import justjabka.JustRacesShowcase.JustRacesShowcase;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.bukkit.damage.DamageType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class DamageTypeTagKeysProvider {
    public static final TagKey<DamageType> IS_MAGIC = create("is_magic");
    public static final TagKey<DamageType> BYPASSES_DODGE = create("bypasses_dodge");
    public static final TagKey<DamageType> BYPASSES_DAMAGE_INVERSION = create("bypasses_damage_inversion");
    public static final TagKey<DamageType> LIZARD_VULNERABLE_TO = create("lizard_vulnerable_to");

    private static TagKey<DamageType> create(@NotNull @KeyPattern.Value String key) {
        return DamageTypeTagKeys.create(Key.key(JustRacesShowcase.NAMESPACE, key));
    }

    @SuppressWarnings("UnstableApiUsage")
    public static Collection<DamageType> getTagValues(TagKey<DamageType> key) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.DAMAGE_TYPE).getTagValues(key);
    }
}
