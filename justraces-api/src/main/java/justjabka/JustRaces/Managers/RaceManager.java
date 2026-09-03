package justjabka.JustRaces.Managers;

import justjabka.JustRaces.Abilities.Generic.ResettableAbility;
import justjabka.JustRaces.Events.Race.Cause;
import justjabka.JustRaces.Events.Race.PlayerRaceChangeEvent;
import justjabka.JustRaces.Events.Race.PlayerRaceChangePreEvent;
import justjabka.JustRaces.Definitions.RaceDefinition;
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

import static justjabka.JustRaces.Managers.ModifierManager.refreshModifiers;

public class RaceManager {
    public static final NamespacedKey RACE_KEY = new NamespacedKey(JustRacesAPI.NAMESPACE, "race");
    public static final NamespacedKey NONE_KEY = new NamespacedKey(JustRacesAPI.NAMESPACE, "none");
    public static final RaceDefinition NONE = getRaceByKey(NONE_KEY);

    /**
     * Gets player's race
     * @param player Player which race will be got
     * @return Race that player has
     * @see #isRace(Player, NamespacedKey)
     */
    @NotNull
    public static RaceDefinition getRace(@NotNull Player player) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        String raceString = pdc.get(RACE_KEY, PersistentDataType.STRING);

        if (raceString == null) return NONE;
        NamespacedKey raceKey = NamespacedKey.fromString(raceString);

        RaceDefinition instance = JustRacesRegistries.RACES.get(raceKey);
        return instance != null ? instance : NONE;
    }

    @NotNull
    public static RaceDefinition getRaceByKey(NamespacedKey key) {
        RaceDefinition instance = JustRacesRegistries.RACES.get(key);
        if (instance == null) {
            throw new IllegalArgumentException("Unregistered race: %s".formatted(key));
        }

        return instance;
    }

    /**
     * Sets player's race
     * @param player Player which would be set
     * @param raceKey Race key
     * @param cause Cause of race change
     * @return {@code true} if race set successfully
     * @see RaceManager#getRaceByKey(NamespacedKey)
     */
    public static boolean setRace(@NotNull Player player, NamespacedKey raceKey, @NotNull Cause cause) {
        RaceDefinition currentRace = getRace(player);
        RaceDefinition newRace = getRaceByKey(raceKey);

        PlayerRaceChangePreEvent pre = new PlayerRaceChangePreEvent(player, currentRace, newRace, cause);
        pre.callEvent();
        if (pre.isCancelled()) return false;

        RaceDefinition finalRace = pre.getNewRace();
        applyRace(pre.getPlayer(), finalRace);

        PlayerRaceChangeEvent post = new PlayerRaceChangeEvent(player, currentRace, finalRace, cause);
        post.callEvent();

        return true;
    }

    /**
     * @see RaceManager#setRace(Player, NamespacedKey, Cause)
     */
    public static boolean setRace(@NotNull Player player, @NotNull RaceDefinition race, @NotNull Cause cause) {
        return setRace(player, race.getKey(), cause);
    }

    private static void applyRace(@NotNull Player player, @NotNull RaceDefinition race) {
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
    public static boolean isRace(@NotNull Player player, NamespacedKey key) {
        RaceDefinition race = getRace(player);
        return race.getKey().equals(key);
    }

    /**
     * @see RaceManager#isRace(Player, NamespacedKey)
     */
    public static boolean isRace(@NotNull Player player, @NotNull RaceDefinition race) {
        return isRace(player, race.getKey());
    }

    /**
     * Checks if player has race
     * @param player Player whose race will be checked
     * @return {@code true} if player has race
     */
    public static boolean hasRace(@NotNull Player player) {
        return !isRace(player, NONE_KEY);
    }

    public static void reloadRace(Player player) {
        RaceDefinition race = getRace(player);
        setRace(player, race, Cause.RELOAD);
    }

    private static void initRace(@NotNull Player player, @NotNull RaceDefinition race) {
        Map<Attribute, Double> attributes = race.getAttributes();
        attributes.forEach((attribute, value) -> AttributeManager.setBaseValue(player, attribute, value));
    }

    /**
     * Resets player's race
     * @param player Player which race would be reset
     */
    public static void resetRace(@NotNull Player player) {
        // Clear potion effects
        player.clearActivePotionEffects();

        // Reset all attributes
        AttributeManager.removeAllModifiers(player);

        Registry.ATTRIBUTE.forEach(attribute -> AttributeManager.resetBaseValue(player, attribute));

        // Disable abilities
        AbilityManager.endAbilities(player, ResettableAbility.Reason.RACE_CHANGE);

        // Reset Item Modifiers
        Bukkit.getScheduler().runTask(JustRacesAPI.getInstance(), () -> refreshModifiers(player));

        // Remove race
        player.getPersistentDataContainer().remove(RACE_KEY);
    }
}