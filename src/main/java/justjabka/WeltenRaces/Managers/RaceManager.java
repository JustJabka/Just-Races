package justjabka.WeltenRaces.Managers;

import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.Abilities.Generic.BaseValidationAbility;
import justjabka.WeltenRaces.DataProvider.RaceProvider;
import justjabka.WeltenRaces.Instances.RaceInstance;
import justjabka.WeltenRaces.Registries.RacesRegistry;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;
import java.util.Set;

import static justjabka.WeltenRaces.Managers.ModifierManager.refreshModifiers;

public class RaceManager {
    public static final NamespacedKey RACE_KEY = new NamespacedKey(WeltenRaces.NAMESPACE, "race");

    public static RaceInstance getRace(Player player) {
        PersistentDataContainer data = player.getPersistentDataContainer();
        String raceString = data.get(RACE_KEY, PersistentDataType.STRING);

        if (raceString == null) {
            return RaceProvider.get(RaceProvider.HUMAN);
        }

        NamespacedKey raceKey = NamespacedKey.fromString(raceString);
        RaceInstance race = RacesRegistry.getRaces().get(raceKey);

        if (race == null) {
            WeltenRaces.LOGGER.warn("Unknown race in PDC for {}: {}", player.getName(), raceString);
            return RaceProvider.get(RaceProvider.HUMAN);
        }

        return race;
    }

    public static void setRace(Player player, NamespacedKey key) {
        resetRace(player);

        PersistentDataContainer data = player.getPersistentDataContainer();
        data.set(RACE_KEY, PersistentDataType.STRING, key.toString());

        RaceInstance race = RacesRegistry.getRaces().get(key);
        initRace(player, race);
    }

    public static boolean isRace(Player player, NamespacedKey key) {
        RaceInstance race = getRace(player);
        return race != null && race.getKey().equals(key);
    }

    private static void initRace(Player player, RaceInstance race) {
        Map<Attribute, Double> attributes = race.getAttributes();
        attributes.forEach((attribute, value) -> AttributeManager.setBaseValue(player, attribute, value));
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
        Set<BaseAbility> allowedAbilities = AbilityManager.getAbilitiesForRace(getRace(player));
        allowedAbilities.forEach(ability -> {
            if (!(ability instanceof BaseValidationAbility validationAbility)) return;
            validationAbility.onDeactivation(player);
        });

        // Reset Item Modifiers
        Bukkit.getScheduler().runTask(WeltenRaces.INSTANCE, () -> refreshModifiers(player));
    }
}