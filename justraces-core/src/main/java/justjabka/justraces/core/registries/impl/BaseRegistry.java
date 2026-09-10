package justjabka.justraces.core.registries.impl;

import justjabka.justraces.api.JustRacesAPI;
import justjabka.justraces.api.interfaces.Registry;
import org.bukkit.NamespacedKey;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public abstract class BaseRegistry<T> implements Registry<T> {
    protected final Map<NamespacedKey, T> storage = new ConcurrentHashMap<>();
    protected final List<BiConsumer<NamespacedKey, T>> hooks = new ArrayList<>();

    public void register(NamespacedKey key, T value) {
        if (storage.containsKey(key)) {
            JustRacesAPI.getLogger().warn("Object with {} is already registered!", key);
        }

        storage.put(key, value);

        for (BiConsumer<NamespacedKey, T> hook : hooks) {
            hook.accept(key, value);
        }
    }

    public void addHook(BiConsumer<NamespacedKey, T> hook) {
        this.hooks.add(hook);
    }

    public T get(NamespacedKey key) {
        return storage.get(key);
    }

    public Collection<T> values() {
        return Collections.unmodifiableCollection(storage.values());
    }

    public Set<NamespacedKey> keys() {
        return Collections.unmodifiableSet(storage.keySet());
    }
}
