package justjabka.weltenRaces.Manager;

import justjabka.weltenRaces.WeltenRaces;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;

public class RaceManager {
    public static final NamespacedKey RACE_KEY = new NamespacedKey(WeltenRaces.PLUGIN_ID, "race");

    public static void setRace(Player player, String raceId) {
        // Reset all attributes
        resetAttributes(player);

        // Clear potion effects
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        // Update race data
        PersistentDataContainer data = player.getPersistentDataContainer();
        data.set(RACE_KEY, PersistentDataType.STRING, raceId);

        // Init race
        if (raceId.equals("armat")) {
            AttributeInstance maxHealth = player.getAttribute(Attribute.MAX_HEALTH);
            if (maxHealth != null) maxHealth.setBaseValue(10.0);
        }
    }

    private static void resetAttributes(Player player) {
        for (Attribute attribute : Registry.ATTRIBUTE) {
            AttributeInstance instance = player.getAttribute(attribute);
            if (instance == null) continue;

            // Remove all attribute modifiers
            instance.getModifiers().forEach(instance::removeModifier);

            // Reset base value of an attribute
            instance.setBaseValue(instance.getDefaultValue());
        }
    }
}