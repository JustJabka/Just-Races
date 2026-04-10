package justjabka.WeltenRaces.Managers;

import justjabka.WeltenRaces.Types.Race;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

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
        resetRace(player);

        // Update race data
        PersistentDataContainer data = player.getPersistentDataContainer();
        data.set(RACE_KEY, PersistentDataType.STRING, race.toString());

        // Init race
        AttributeInstance scaleInstance = player.getAttribute(Attribute.SCALE);
        AttributeInstance maxHealthInstance = player.getAttribute(Attribute.MAX_HEALTH);

        switch (race) {
            case ARMAT -> {
                modifyBaseValue(scaleInstance, 1);
                modifyBaseValue(maxHealthInstance, 10);
            }
            case HUMAN -> {
                modifyBaseValue(scaleInstance, 1);
                modifyBaseValue(maxHealthInstance, 26);
            }
        }
    }

    public static void resetRace(Player player) {
        // Clear potion effects
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));

        // Reset all attributes
        for (Attribute attribute : Registry.ATTRIBUTE) {
            AttributeInstance instance = player.getAttribute(attribute);

            if (instance == null) continue;

            // Remove all modifiers
            for (AttributeModifier modifier : instance.getModifiers()) {
                String modifierNamespace = modifier.getKey().getNamespace();

                if (modifierNamespace.equals(NamespacedKey.MINECRAFT)) continue;

                instance.removeModifier(modifier);
            }
        }
    }

    private static void modifyBaseValue(AttributeInstance instance, double newValue) {
        if (instance == null) return;

        NamespacedKey key = new NamespacedKey(WeltenRaces.PLUGIN_ID, instance.getAttribute().getKey().getKey());
        double baseValue = instance.getBaseValue();

        double diff = newValue - baseValue;

        if (diff == 0) return;

        AttributeModifier modifier = new AttributeModifier(
                key,
                diff,
                AttributeModifier.Operation.ADD_NUMBER
        );
        instance.addModifier(modifier);
    }
}