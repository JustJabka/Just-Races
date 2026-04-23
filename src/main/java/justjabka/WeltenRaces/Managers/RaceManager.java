package justjabka.WeltenRaces.Managers;

import justjabka.WeltenRaces.Types.Race;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static justjabka.WeltenRaces.Managers.ModifierManager.refreshModifiers;

public class RaceManager {
    public static final NamespacedKey RACE_KEY = new NamespacedKey(WeltenRaces.PLUGIN_ID, "race");

    public static Race getRace(Player player) {
        PersistentDataContainer data = player.getPersistentDataContainer();
        String race = data.get(RACE_KEY, PersistentDataType.STRING);

        if (race == null) return Race.HUMAN;

        try {
            return Race.valueOf(race.toUpperCase());
        } catch (IllegalArgumentException e) {
            WeltenRaces.LOGGER.warn("Unknown race in PDC for {}: {}", player.getName(), race);
            return Race.HUMAN;
        }
    }

    public static void setRace(Player player, Race race) {
        resetRace(player);

        // Update race data
        PersistentDataContainer data = player.getPersistentDataContainer();
        data.set(RACE_KEY, PersistentDataType.STRING, race.toString());

        initRace(player, race);
    }

    private static void initRace(Player player, Race race) {
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

        // Reset Item Modifiers
        Bukkit.getScheduler().runTask(WeltenRaces.INSTANCE, () -> refreshModifiers(player));
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