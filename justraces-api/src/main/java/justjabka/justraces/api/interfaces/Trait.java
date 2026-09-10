package justjabka.justraces.api.interfaces;

import justjabka.justraces.api.managers.TraitManager;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

public interface Trait extends PersistentHolder {
    NamespacedKey getKey();

    @Override
    default NamespacedKey getContainerKey() {
        return TraitManager.TRAITS_CONTAINER_KEY;
    }

    default boolean isRequiredTrait(Player player) {
        return TraitManager.getTraitsForPlayer(player).contains(this);
    }
}
