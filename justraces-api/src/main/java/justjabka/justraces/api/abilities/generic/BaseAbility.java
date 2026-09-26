package justjabka.justraces.api.abilities.generic;

import justjabka.justraces.api.JustRacesAPI;
import justjabka.justraces.api.abilities.AbilityContext;
import justjabka.justraces.api.abilities.AbilityTrigger;
import justjabka.justraces.api.abilities.AbilityTriggerCondition;
import justjabka.justraces.api.common.PersistentHolder;
import justjabka.justraces.api.common.TimerBar;
import justjabka.justraces.api.events.ability.PlayerAbilityTriggerEvent;
import justjabka.justraces.api.events.ability.PlayerAbilityTriggerPreEvent;
import justjabka.justraces.api.managers.AbilityManager;
import justjabka.justraces.api.managers.TimeManager;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Set;

public abstract class BaseAbility implements Listener, TimerBar, PersistentHolder {

    private static final NamespacedKey COOLDOWN = new NamespacedKey(JustRacesAPI.NAMESPACE, "cooldown");
    private static final NamespacedKey EXPIRES_AT = new NamespacedKey(JustRacesAPI.NAMESPACE, "expires_at");

    public abstract NamespacedKey getKey();
    public abstract long getCooldownTicks();

    public AbilityTrigger getDefaultTrigger() {
        return AbilityTrigger.CUSTOM;
    }

    public Set<AbilityTriggerCondition> getDefaultTriggerConditions() {
        return Set.of();
    }

    @Override
    public float getBarProgress(Player player) {
        float remainingTicks = (float) getRemainingTicks(player);
        if (remainingTicks <= 0) return 0f;

        float cooldownTicks = (float) getEntryLong(player, COOLDOWN);
        if (cooldownTicks <= 0) return 0f;

        return remainingTicks / cooldownTicks;
    }

    @Override
    public boolean shouldBarDisplay(Player player) {
        return isOnCooldown(player);
    }

    @Override
    public NamespacedKey getContainerKey() {
        return AbilityManager.ABILITIES_CONTAINER_KEY;
    }

    /**
     * Gets remaining ticks that ability need to recharge
     * @param player Player for which we are getting the remaining ticks
     * @return Remaining ticks
     * @see #getRemainingSeconds(Player)
     */
    public long getRemainingTicks(Player player) {
        return TimeManager.getRemainingExpireStampTicks(getExpireStamp(player));
    }

    /**
     * Gets remaining seconds that ability need to recharge
     * @param player Player for which we are getting the remaining seconds
     * @return Remaining seconds
     * @see #getRemainingTicks(Player)
     */
    public long getRemainingSeconds(Player player) {
        final float remainingSeconds = getRemainingTicks(player) / 20f;
        return (long) remainingSeconds;
    }

    /**
     * Checks if ability is on cooldown
     * @param player Player for which we are checking the ability
     * @return {@code true} if ability is on cooldown
     */
    public boolean isOnCooldown(Player player) {
        return TimeManager.isExpireStampValid(getExpireStamp(player));
    }

    /**
     * Puts ability on cooldown
     * @param player Player for which we set the ability on cooldown
     * @see #setCooldownTicks(Player, long)
     * @see #getCooldownTicks()
     */
    public void putOnCooldown(Player player) {
        setCooldownTicks(player, getCooldownTicks());
    }

    /**
     * Resets the active cooldown for the specified player, making the ability instantly available.
     *
     * @param player the player whose ability cooldown is being reset
     * @see #setCooldownTicks(Player, long)
     * @see #getCooldownTicks()
     */
    public void resetCooldown(Player player) {
        removeEntryData(player, COOLDOWN);
        removeEntryData(player, EXPIRES_AT);
    }

    /**
     * Sets new cooldown for the ability
     * @param player Player for which we are setting the ability cooldown
     * @param newCooldown Cooldown that be set
     * @apiNote This method don't override ability original cooldown
     * @see #putOnCooldown(Player)
     * @see #getCooldownTicks()
     */
    public void setCooldownTicks(Player player, long newCooldown) {
        final long expiresAt = TimeManager.getExpireStamp(newCooldown);

        setEntryLong(player, COOLDOWN, newCooldown);
        setEntryLong(player, EXPIRES_AT, expiresAt);
    }

    /**
     * Gets gametime stamp when player ability will be recharged
     * @param player Player from which we are getting ability cooldown
     * @return expire stamp
     */
    private long getExpireStamp(Player player) {
        return getEntryLong(player, EXPIRES_AT);
    }

    /**
     * Checks if player has this ability
     * @param player Player
     * @return {@code true} if player has this ability
     */
    public boolean isRequiredAbility(Player player) {
        return AbilityManager.playerHasAbility(player, this);
    }

    /**
     * The additional conditions that needed for the ability activation
     * @param player Player for which the ability activation is checked
     * @return {@code true} if ability can be activated
     */
    protected boolean canActivate(Player player) {
        return true;
    }

    /**
     * Tries to activate the ability
     * @param player Player for which the ability is tried to be to activated
     * @return {@code true} if ability activated successfully
     * @see #onActivation(Player, AbilityContext)
     */
    public boolean tryActivate(Player player) {
        return tryActivate(player, AbilityContext.ofEmpty());
    }

    /**
     * Tries to activate the ability
     * @param player Player for which the ability is tried to be to activated
     * @param ctx Additional context of the ability
     * @return {@code true} if ability activated successfully
     * @see #onActivation(Player, AbilityContext)
     */
    public boolean tryActivate(Player player, AbilityContext ctx) {
        // Validate
        if (!isRequiredAbility(player)) return false;
        if (!canActivate(player)) return false;
        if (isOnCooldown(player)) return false;

        // Pre Event
        PlayerAbilityTriggerPreEvent preEvent = new PlayerAbilityTriggerPreEvent(
                player,
                this,
                ctx
        );
        if (!preEvent.callEvent()) return false;

        // Activate
        if (!onActivation(player, ctx)) return false;
        putOnCooldown(player);

        // Post Event
        PlayerAbilityTriggerEvent postEvent = new PlayerAbilityTriggerEvent(player, this, ctx);
        postEvent.callEvent();

        return true;
    }

    /**
     * Will be executed on ability activation
     * @param player Player for which the ability will be activated
     * @param ctx Additional context of the ability
     * @return {@code true} if ability activated successfully. {@code false} to cancel ability activation
     * @see #tryActivate(Player)
     */
    protected abstract boolean onActivation(Player player, AbilityContext ctx);
}