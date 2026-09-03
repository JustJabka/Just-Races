package justjabka.JustRaces.Interfaces;

import justjabka.JustRaces.Managers.TraitManager;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

public interface Trait {
    NamespacedKey getKey();

    default boolean isRequiredTrait(Player player) {
        return TraitManager.getTraitsForPlayer(player).contains(this);
    }
}
