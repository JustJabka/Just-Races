package justjabka.weltenRaces.Managers;

import justjabka.weltenRaces.Types.ArmorSet;
import justjabka.weltenRaces.WeltenRaces;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ArmorManager {
    private static final NamespacedKey ARMOR_SET_KEY = new NamespacedKey(WeltenRaces.PLUGIN_ID, "armor_set");

    public static void updateArmorSet(Player player) {
        ItemStack[] equipment = player.getEquipment().getArmorContents();

        // Set armor set to NONE if armor is incomplete
        for (ItemStack item : equipment) {
            if (item == null || item.isEmpty()) {
                setStoredArmor(player, ArmorSet.NONE);
                return;
            }
        }

        // Get first item piece
        ArmorSet firstType = ArmorSet.fromMaterialName(equipment[0].getType().name());

        // Check if all armor pieces are the same
        for (int i = 1; i < equipment.length; i++) {
            ArmorSet currentType = ArmorSet.fromMaterialName(equipment[i].getType().name());

            if (currentType != firstType) {
                setStoredArmor(player, ArmorSet.NONE);
                return;
            }
        }

        setStoredArmor(player, firstType);
    }

    public static boolean hasAnyArmor(Player player) {
        ItemStack[] equipment = player.getEquipment().getArmorContents();

        for (ItemStack item : equipment) {
            if (item == null) continue;
            if (item.isEmpty()) continue;

            return true;
        }
        return false;
    }

    public static boolean hasArmorSet(Player player, ArmorSet expected) {
        PersistentDataContainer data = player.getPersistentDataContainer();
        String stored = data.get(ARMOR_SET_KEY, PersistentDataType.STRING);

        if (stored == null) {
            return expected == ArmorSet.NONE;
        }

        return stored.equals(expected.toString());
    }

    private static void setStoredArmor(Player player, ArmorSet set) {
        player.getPersistentDataContainer().set(
                ARMOR_SET_KEY,
                PersistentDataType.STRING,
                set.toString()
        );
    }
}
