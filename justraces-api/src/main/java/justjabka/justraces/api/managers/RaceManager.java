package justjabka.justraces.api.managers;

import justjabka.justraces.api.JustRacesAPI;
import justjabka.justraces.api.JustRacesRegistries;
import justjabka.justraces.api.abilities.generic.ResettableAbility;
import justjabka.justraces.api.common.definition.RaceDefinition;
import justjabka.justraces.api.common.entry.AttributeEntry;
import justjabka.justraces.api.events.race.Cause;
import justjabka.justraces.api.events.race.PlayerRaceChangeEvent;
import justjabka.justraces.api.events.race.PlayerRaceChangePreEvent;
import justjabka.justraces.api.traits.generic.ResettableTrait;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public final class RaceManager {

    private RaceManager() {}

    public static final NamespacedKey RACE_KEY = new NamespacedKey(JustRacesAPI.NAMESPACE, "race");

    public static final NamespacedKey NONE_KEY = new NamespacedKey(JustRacesAPI.NAMESPACE, "none");
    public static final RaceDefinition NONE = getByKey(NONE_KEY);

    /**
     * Gets player's race
     * @param player Player which race will be got
     * @return Race that player has
     * @see #isRace(Player, NamespacedKey)
     */
    public static RaceDefinition getRace(Player player) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        String raceString = pdc.get(RACE_KEY, PersistentDataType.STRING);

        if (raceString == null) return NONE;
        NamespacedKey raceKey = NamespacedKey.fromString(raceString);

        RaceDefinition instance = JustRacesRegistries.RACES.get(raceKey);
        return instance != null ? instance : NONE;
    }

    public static RaceDefinition getByKey(NamespacedKey key) {
        RaceDefinition instance = JustRacesRegistries.RACES.get(key);
        if (instance == null) {
            throw new IllegalArgumentException("Unregistered Race: %s".formatted(key));
        }

        return instance;
    }

    /**
     * Sets player's race
     * @param player Player which would be set
     * @param raceKey Race key
     * @param cause Cause of race change
     * @return {@code true} if race set successfully
     * @see setRace(Player, RaceDefinition, Cause)
     * @see getByKey(NamespacedKey)
     */
    public static boolean setRace(Player player, NamespacedKey raceKey, Cause cause) {
        RaceDefinition currentRace = getRace(player);
        RaceDefinition newRace = getByKey(raceKey);

        // Pre Event
        PlayerRaceChangePreEvent preEvent = new PlayerRaceChangePreEvent(
                player,
                currentRace,
                newRace,
                cause
        );
        if (!preEvent.callEvent()) return false;

        // Change Race
        RaceDefinition finalRace = preEvent.getNewRace();
        applyRace(preEvent.getPlayer(), finalRace);

        // Post Event
        PlayerRaceChangeEvent postEvent = new PlayerRaceChangeEvent(player, currentRace, finalRace, cause);
        postEvent.callEvent();

        return true;
    }

    private static void applyRace(Player player, RaceDefinition race) {
        resetRace(player);

        PersistentDataContainer data = player.getPersistentDataContainer();
        data.set(RACE_KEY, PersistentDataType.STRING, race.getKey().asString());

        initRace(player, race);
    }

    private static void initRace(Player player, RaceDefinition race) {
        applyRaceAttributes(player, race);
        TraitManager.startTraits(player);
    }

    private static void applyRaceAttributes(Player player, RaceDefinition race) {
        List<AttributeEntry> attributes = race.getAttributeEntries();

        for (AttributeEntry attributeEntry : attributes) {
            Attribute attribute = attributeEntry.attribute();
            double amount = attributeEntry.amount();

            if (attributeEntry.isBaseValue()) {
                AttributeManager.setBaseValue(player, attribute, amount);
            } else if (attributeEntry.isModifier()) {
                AttributeModifier modifier = attributeEntry.createModifier(race.getKey());
                AttributeManager.addModifier(player, attribute, modifier);
            }
        }
    }

    /**
     * Sets player's race
     * @param player Player which would be set
     * @param race Race
     * @param cause Cause of race change
     * @return {@code true} if race set successfully
     * @see setRace(Player, NamespacedKey, Cause)
     */
    public static boolean setRace(Player player, RaceDefinition race, Cause cause) {
        return setRace(player, race.getKey(), cause);
    }

    /**
     * Check if player's race matches to provided
     * @param player Player which race will be checked
     * @param key Race key
     * @return {@code true} if races matches
     * @see isRace(Player, RaceDefinition)
     */
    public static boolean isRace(Player player, NamespacedKey key) {
        RaceDefinition race = getRace(player);
        return race.getKey().equals(key);
    }

    /**
     * Check if player's race matches to provided
     * @param player Player which race will be checked
     * @param race Race
     * @return {@code true} if races matches
     * @see isRace(Player, NamespacedKey)
     */
    public static boolean isRace(Player player, RaceDefinition race) {
        return isRace(player, race.getKey());
    }

    /**
     * Checks if player has race
     * @param player Player whose race will be checked
     * @return {@code true} if player has race
     */
    public static boolean hasRace(Player player) {
        return !isRace(player, NONE_KEY);
    }

    /**
     * Reloads player's race
     * @param player Player which race would be reloaded
     * @apiNote Don't confuse with {@link resetRace(Player)} and {@link resyncRace(Player)}
     */
    public static void reloadRace(Player player) {
        RaceDefinition race = getRace(player);
        setRace(player, race, Cause.RELOAD);
    }

    /**
     * Resyncs player's race
     * @param player Player which race would be resynced
     * @apiNote Don't confuse with {@link resetRace(Player)} and {@link reloadRace(Player)}
     */
    public static void resyncRace(Player player) {
        resetAttributes(player);
        initRace(player, getRace(player));
    }

    /**
     * Resets player's race
     * @param player Player which race would be reset
     * @apiNote Don't confuse with {@link reloadRace(Player)} and {@link resyncRace(Player)}
     */
    public static void resetRace(Player player) {
        resetAttributes(player);
        endEverything(player);
        player.getPersistentDataContainer().remove(RACE_KEY);
    }

    private static void resetAttributes(Player player) {
        AttributeManager.removeAllModifiers(player);
        AttributeManager.resetAllBaseValues(player);
    }

    private static void endEverything(Player player) {
        AbilityManager.endAbilities(player, ResettableAbility.Reason.RACE_CHANGE);
        TraitManager.endTraits(player, ResettableTrait.Reason.RACE_CHANGE);
        TransientManager.resetTransientContainer(player);

        Bukkit.getScheduler().runTask(JustRacesAPI.getInstance(), () -> ItemModifierManager.refreshModifiers(player));
    }
}