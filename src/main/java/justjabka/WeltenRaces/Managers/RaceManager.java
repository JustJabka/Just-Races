package justjabka.WeltenRaces.Managers;

import justjabka.WeltenRaces.Types.Race;
import justjabka.WeltenRaces.WeltenRaces;
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

    public static String getRace(Player player) {
        PersistentDataContainer data = player.getPersistentDataContainer();
        String race = data.get(RACE_KEY, PersistentDataType.STRING);

        return race != null ? race : "none";
    }

    public static boolean raceEquals(Player player, Race race) {
        return race.toString().equals(getRace(player));
    }

    public static void setRace(Player player, Race race) {
        // Reset all attributes
        resetAttributes(player);

        // Clear potion effects
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        // Update race data
        PersistentDataContainer data = player.getPersistentDataContainer();
        data.set(RACE_KEY, PersistentDataType.STRING, race.toString());

        // Init race
        if (race == Race.ARMAT) {
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