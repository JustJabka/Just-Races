package justjabka.justraces.api.common.registry;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.Collection;
import java.util.Set;
import java.util.function.BiConsumer;

@NullMarked
public interface Registry<T> {
    void register(NamespacedKey key, T value);
    void addHook(BiConsumer<NamespacedKey, T> hook);

    @Nullable T get(NamespacedKey key);
    T getOrThrow(NamespacedKey key);

    Collection<T> values();
    Set<NamespacedKey> keys();
}