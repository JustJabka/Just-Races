package justjabka.WeltenRaces.Managers;

import justjabka.WeltenRaces.Abilities.BaseAbility;
import justjabka.WeltenRaces.Abilities.DamageInversion;
import justjabka.WeltenRaces.Abilities.Ecdysis;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class AbilityManager {
    public static final NamespacedKey ABILITIES_CONTAINER_KEY = new NamespacedKey(WeltenRaces.NAMESPACE, "abilities");

    public void loadAbilityListeners() {
        List<BaseAbility> abilities = List.of(
                new DamageInversion(),
                new Ecdysis()
        );

        for (BaseAbility ability : abilities) {
            Bukkit.getPluginManager().registerEvents(ability, WeltenRaces.INSTANCE);
        }

        WeltenRaces.LOGGER.info("Successfully loaded abilities!");
    }

    public static PersistentDataContainer getAbilities(Player player) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        PersistentDataContainer abilities = pdc.getOrDefault(
                ABILITIES_CONTAINER_KEY,
                PersistentDataType.TAG_CONTAINER,
                pdc.getAdapterContext().newPersistentDataContainer()
        );

        return abilities;
    }

    public static boolean isAbilityActive(Player player, NamespacedKey key) {
        return AbilityManager.getAbilities(player).getOrDefault(key, PersistentDataType.BOOLEAN, false);
    }

    public static void updateAbilities(Player player, PersistentDataContainer abilities) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        pdc.set(
                ABILITIES_CONTAINER_KEY,
                PersistentDataType.TAG_CONTAINER,
                abilities
        );
    }

    public static boolean hasActivationSlotSelected(Player player) {
        int hotbarSlot = player.getInventory().getHeldItemSlot();
        return hotbarSlot == 8;
    }
}
