package justjabka.WeltenRaces.Managers;

import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.Abilities.Generic.BaseValidationAbility;
import justjabka.WeltenRaces.Types.Race;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Set;

import static justjabka.WeltenRaces.Managers.ModifierManager.refreshModifiers;

public class RaceManager {
    public static final NamespacedKey RACE_KEY = new NamespacedKey(WeltenRaces.NAMESPACE, "race");

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
        switch (race) {
            case ARMAT -> {
                AttributeManager.setBaseValue(player, Attribute.MAX_HEALTH, 10);
            }
            case HUMAN -> {
                AttributeManager.setBaseValue(player, Attribute.MAX_HEALTH, 26);
            }
            case SKYZERN -> {
                AttributeManager.setBaseValue(player, Attribute.SCALE, 1.05);
            }
            case EPIPHYTE -> {
                AttributeManager.setBaseValue(player, Attribute.SCALE, 0.95);
                AttributeManager.setBaseValue(player, Attribute.MAX_HEALTH, 18);
            }
            case LIZARD -> {
                AttributeManager.setBaseValue(player, Attribute.SCALE, 1);
                AttributeManager.setBaseValue(player, Attribute.MAX_HEALTH, 14);
                AttributeManager.setBaseValue(player, Attribute.FALL_DAMAGE_MULTIPLIER, 0.8);
            }
        }
    }

    public static void resetRace(Player player) {
        // Clear potion effects
        player.clearActivePotionEffects();

        // Reset all attributes
        AttributeManager.removeAllModifiers(player);

        for (Attribute attribute : Registry.ATTRIBUTE) {
            AttributeManager.resetBaseValue(player, attribute);
        }

        // Disable abilities
        Set<BaseAbility> allowedAbilities = AbilityManager.getAbilitiesForRace(RaceManager.getRace(player));
        allowedAbilities.forEach(ability -> {
            if (!(ability instanceof BaseValidationAbility validationAbility)) return;
            validationAbility.onDeactivation(player);
        });

        // Reset Item Modifiers
        Bukkit.getScheduler().runTask(WeltenRaces.INSTANCE, () -> refreshModifiers(player));
    }
}