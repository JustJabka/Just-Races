package justjabka.WeltenRaces.DataProvider;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.keys.tags.EntityTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import justjabka.WeltenRaces.WeltenRaces;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class EntityTypeTagKeysProvider {
    public static final TagKey<EntityType> GLUTTONY_EXECUTE_IGNORED = create("gluttony_execute_ignored");

    private static TagKey<EntityType> create(@NotNull @KeyPattern.Value String key) {
        return EntityTypeTagKeys.create(Key.key(WeltenRaces.NAMESPACE, key));
    }

    @SuppressWarnings("UnstableApiUsage")
    public static Collection<EntityType> getTagValues(TagKey<EntityType> key) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.ENTITY_TYPE).getTagValues(key);
    }
}
