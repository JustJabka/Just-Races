package justjabka.justraces.api.managers;

import justjabka.justraces.api.JustRacesAPI;
import justjabka.justraces.api.types.ArmorSet;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ArmorManager {
    private static final NamespacedKey ARMOR_SET_KEY = new NamespacedKey(JustRacesAPI.NAMESPACE, "armor_set");

    /**
     * Updates player's armor set
     * @param player Player whose armor set will be updated
     */
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
        ArmorSet firstType = ArmorSet.fromMaterialName(equipment[0].getType().name().toLowerCase());

        // Check if all armor pieces are the same
        for (int i = 1; i < equipment.length; i++) {
            ArmorSet currentType = ArmorSet.fromMaterialName(equipment[i].getType().name().toLowerCase());

            if (currentType != firstType) {
                setStoredArmor(player, ArmorSet.NONE);
                return;
            }
        }

        setStoredArmor(player, firstType);
    }

    /**
     * Checks if player has any armor equipped
     * @param player Player whose equipment will be checked
     * @return {@code true} if player has any armor
     */
    public static boolean hasAnyArmor(Player player) {
        ItemStack[] equipment = player.getEquipment().getArmorContents();

        for (ItemStack item : equipment) {
            if (item == null) continue;
            if (item.isEmpty()) continue;

            return true;
        }
        return false;
    }

    /**
     * Gets armor set that player is currently wearing
     * @param player Player whose armor set will be got
     * @return Player's armor set. {@code ArmorSet.NONE} if the armor type doesn't match or missing
     */
    public static ArmorSet getArmorSet(Player player) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        String stored = pdc.get(ARMOR_SET_KEY, PersistentDataType.STRING);

        if (stored == null) return ArmorSet.NONE;

        try {
            return ArmorSet.valueOf(stored.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ArmorSet.NONE;
        }
    }

    private static void setStoredArmor(Player player, ArmorSet set) {
        player.getPersistentDataContainer().set(
                ARMOR_SET_KEY,
                PersistentDataType.STRING,
                set.toString()
        );
    }

    /**
     * Gets armor average durability percent.
     * <p>
     * For every piece: {@code (maxDamage - currentDamage) / maxDamage} and divided by total armor count (4 for full armor set)
     * @param player Player whose armor durability will calced
     * @return Average armor durability percent (from 0.0 to 1.0). 0.0 if no armor equipped
     */
    public static double getAverageDurability(Player player) {
        int count = 0;
        double totalPercent = 0;

        ItemStack[] equipment = player.getEquipment().getArmorContents();

        for (ItemStack item : equipment) {
            if (item == null) continue;
            if (item.isEmpty()) continue;

            if (!(item.getItemMeta() instanceof Damageable itemMeta)) continue;

            int maxDamage = itemMeta.hasMaxDamage()
                    ? itemMeta.getMaxDamage()
                    : item.getType().getMaxDurability();
            int currentDamage = itemMeta.getDamage();

            double percent = (double) (maxDamage - currentDamage) / maxDamage;

            totalPercent += percent;
            count++;
        }

        return count == 0 ? 0 : (totalPercent / count);
    }
}