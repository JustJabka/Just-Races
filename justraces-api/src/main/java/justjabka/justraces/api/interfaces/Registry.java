package justjabka.justraces.api.interfaces;

import org.bukkit.NamespacedKey;

import java.util.Collection;
import java.util.Set;
import java.util.function.BiConsumer;

public interface Registry<T> {
    void register(NamespacedKey key, T value);

    void addHook(BiConsumer<NamespacedKey, T> hook) ;

    T get(NamespacedKey key);

    Collection<T> values();

    Set<NamespacedKey> keys();
}