package justjabka.WeltenRaces.DataProvider;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import justjabka.WeltenRaces.WeltenRaces;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class ItemTypeTagKeysProvider {
    public static final TagKey<ItemType> IS_MEAT = create("is_meat");

    private static TagKey<ItemType> create(@NotNull @KeyPattern.Value String key) {
        return ItemTypeTagKeys.create(Key.key(WeltenRaces.NAMESPACE, key));
    }

    @SuppressWarnings("UnstableApiUsage")
    public static Collection<ItemType> getTagValues(TagKey<ItemType> key) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.ITEM).getTagValues(key);
    }
}
