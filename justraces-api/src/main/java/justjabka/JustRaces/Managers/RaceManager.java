package justjabka.JustRaces.Managers;

import justjabka.JustRaces.Abilities.Generic.BaseAbility;
import justjabka.JustRaces.Abilities.Generic.BaseValidationAbility;
import justjabka.JustRaces.Events.Race.PlayerRaceChangePreEvent;
import justjabka.JustRaces.Instances.RaceInstance;
import justjabka.JustRaces.JustRacesAPI;
import justjabka.JustRaces.JustRacesRegistries;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

import static justjabka.JustRaces.Managers.ModifierManager.refreshModifiers;

public class RaceManager {
    public static final NamespacedKey RACE_KEY = new NamespacedKey(JustRacesAPI.NAMESPACE, "race");
    public static final NamespacedKey FALLBACK_RACE = new NamespacedKey("justracesshowcase","human");

    /**
     * Gets player's race
     * @param player Player which race will be got
     * @return Race that player has
     * @see #isRace(Player, NamespacedKey)
     */
    public static RaceInstance getRace(Player player) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        String raceString = pdc.get(RACE_KEY, PersistentDataType.STRING);

        if (raceString == null) {
            return getFallbackRace();
        }

        NamespacedKey raceKey = NamespacedKey.fromString(raceString);
        RaceInstance race = getRaceByKey(raceKey);

        if (race == null) {
            JustRacesAPI.getLogger().warn("Unknown race in PDC for {}: {}", player.getName(), raceString);
            return getFallbackRace();
        }

        return race;
    }

    public static RaceInstance getRaceByKey(NamespacedKey key) {
        return JustRacesRegistries.RACES.get(key);
    }

    public static RaceInstance getFallbackRace() {
        return getRaceByKey(FALLBACK_RACE);
    }

    /**
     * Sets player's race
     * @param player Player which would be set
     * @param raceKey Race key
     * @param cause Cause of race change
     * @return {@code true} if race set successfully
     * @see RaceManager#getRaceByKey(NamespacedKey)
     */
    public static boolean setRace(Player player, NamespacedKey raceKey, PlayerRaceChangePreEvent.Cause cause) {
        RaceInstance oldRace = RaceManager.getRace(player);
        RaceInstance newRace = getRaceByKey(raceKey);

        if (newRace == null) {
            JustRacesAPI.getLogger().warn("Tried to set unknown race for {}: {}", player.getName(), raceKey);
            return false;
        }

        PlayerRaceChangePreEvent event = new PlayerRaceChangePreEvent(player, oldRace, newRace, cause);
        event.callEvent();
        if (event.isCancelled()) return false;

        applyRace(event.getPlayer(), event.getNewRace());
        return true;
    }

    /**
     * @see RaceManager#setRace(Player, NamespacedKey, PlayerRaceChangePreEvent.Cause)
     */
    public static boolean setRace(Player player, RaceInstance race, PlayerRaceChangePreEvent.Cause cause) {
        return setRace(player, race.getKey(), cause);
    }

    private static void applyRace(Player player, @NotNull RaceInstance race) {
        resetRace(player);

        PersistentDataContainer data = player.getPersistentDataContainer();
        data.set(RACE_KEY, PersistentDataType.STRING, race.getKey().asString());

        initRace(player, race);
    }

    /**
     * Check if player's race matches to provided
     * @param player Player which race will be checked
     * @param key Race key
     * @return {@code true} if races matches
     */
    public static boolean isRace(Player player, NamespacedKey key) {
        RaceInstance race = getRace(player);
        return race != null && race.getKey().equals(key);
    }

    private static void initRace(Player player, RaceInstance race) {
        Map<Attribute, Double> attributes = race.getAttributes();
        attributes.forEach((attribute, value) -> AttributeManager.setBaseValue(player, attribute, value));
    }

    /**
     * Resets player's race
     * @param player Player which race would be reseted
     * @apiNote use this carefully because race will become {@code null}!
     */
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
        Bukkit.getScheduler().runTask(JustRacesAPI.getInstance(), () -> refreshModifiers(player));
    }
}