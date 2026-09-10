package justjabka.justraces.api.interfaces;

import justjabka.justraces.api.managers.RaceManager;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

public interface RaceContext extends PersistentHolder {
    NamespacedKey getKey();

    @Override
    default NamespacedKey getContainerKey() {
        return RaceManager.RACES_CONTAINER_KEY;
    }

    default boolean isRequiredRace(Player player) {
        return RaceManager.isRace(player, getKey());
    }
}
