package justjabka.JustRaces.Abilities.Generic;

import justjabka.JustRaces.JustRacesAPI;
import justjabka.JustRaces.Managers.AbilityManager;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.ShadowColor;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseAbility implements Listener {
    private final Map<UUID, Long> cooldowns = new ConcurrentHashMap<>();
    private final Map<UUID, BossBar> cooldownBars = new ConcurrentHashMap<>();

    protected static final Key COOLDOWN_BAR_FONT = Key.key(JustRacesAPI.NAMESPACE, "cooldown");
    protected static final Component COOLDOWN_BAR_ICON_OFFSET = Component.text("\uDB00\uDCC6").font(COOLDOWN_BAR_FONT);

    public abstract NamespacedKey getKey();
    public abstract long getCooldownTicks();

    public BossBar.Color getCooldownBarColor(Player player) {
        return BossBar.Color.WHITE;
    }
    public Component getCooldownBarIcon(Player player) {
        return Component.text("\uE000")
                .font(COOLDOWN_BAR_FONT);
    }

    /**
     * Gets remaining ticks that ability need to recharge
     * @param player Player for which we are getting the remaining ticks
     * @return Remaining ticks
     * @see #getRemainingSeconds(Player)
     */
    public long getRemainingTicks(Player player) {
        long remaining = getExpireStamp(player) - getGameTime();
        return Math.max(0, remaining);
    }

    /**
     * Gets remaining seconds that ability need to recharge
     * @param player Player for which we are getting the remaining seconds
     * @return Remaining seconds
     * @see #getRemainingTicks(Player)
     */
    public long getRemainingSeconds(Player player) {
        float remainingSeconds = getRemainingTicks(player) / 20f;

        return (long) remainingSeconds;
    }

    /**
     * Checks if ability is on cooldown
     * @param player Player for which we are checking the ability
     * @return {@code true} if ability is on cooldown
     */
    public boolean isOnCooldown(Player player) {
        return getGameTime() < getExpireStamp(player);
    }

    /**
     * Puts ability on cooldown
     * @param player Player for which we set the ability on cooldown
     * @see #setCooldownTicks(Player, long)
     * @see #getCooldownTicks()
     */
    public void putOnCooldown(Player player) {
        long expiresAt = getGameTime() + getCooldownTicks();
        setCooldownTicks(player, expiresAt);
    }

    /**
     * Resets the active cooldown for the specified player, making the ability instantly available.
     *
     * @param player the player whose ability cooldown is being reset
     * @see #setCooldownTicks(Player, long)
     * @see #getCooldownTicks()
     */
    public void resetCooldown(Player player) {
        cooldowns.remove(player.getUniqueId());
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
        cooldowns.put(player.getUniqueId(), newCooldown);
    }

    /**
     * Gets gametime stamp when player ability will be recharged
     * @param player Player from which we are getting ability cooldown
     * @return expire stamp
     */
    private Long getExpireStamp(Player player) {
        return cooldowns.getOrDefault(player.getUniqueId(), 0L);
    }

    /**
     * Gets gametime from the overworld
     * @return Gametime
     */
    protected static long getGameTime() {
        return Bukkit.getWorlds().getFirst().getGameTime();
    }

    /**
     * Check if player has this ability
     * @param player Player that will be checked
     * @return {@code true} if player has this ability
     */
    public boolean playerHasAbility(Player player) {
        return AbilityManager.getAbilitiesForPlayer(player).contains(this);
    }

    public void updateCooldownBar(Player player) {
        float remainingTicks = (float) getRemainingTicks(player);
        float cooldownTicks = (float) getCooldownTicks();

        if (remainingTicks <= 0 || cooldownTicks <= 0) {
            removeCooldownBar(player);
            return;
        }

        float progress = Math.clamp(remainingTicks / cooldownTicks, BossBar.MIN_PROGRESS, BossBar.MAX_PROGRESS);
        final Component iconWithOffset = getCooldownBarIcon(player).shadowColor(ShadowColor.none()).append(COOLDOWN_BAR_ICON_OFFSET);
        final BossBar.Color color = getCooldownBarColor(player);

        BossBar cooldownBar = cooldownBars.computeIfAbsent(player.getUniqueId(), uuid -> {
            BossBar bar = BossBar.bossBar(iconWithOffset, progress, color, BossBar.Overlay.NOTCHED_6);
            player.showBossBar(bar);
            return bar;
        });

        cooldownBar.name(iconWithOffset);
        cooldownBar.color(color);
        cooldownBar.progress(progress);
    }

    protected void removeCooldownBar(Player player) {
        BossBar bossBar = cooldownBars.remove(player.getUniqueId());
        if (bossBar == null) return;
        player.hideBossBar(bossBar);
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
     * @param ctx Context of the activation. Literally any {@link Object }
     * @see #onActivation(Player, Object...)
     */
    protected void tryActivate(Player player, Object... ctx) {
        if (!playerHasAbility(player)) return;

        if (!canActivate(player)) return;

        if (isOnCooldown(player)) return;

        if (!onActivation(player, ctx)) return;
        putOnCooldown(player);
    }

    /**
     * Will be executed on ability activation
     * @param player Player for which the ability will be activated
     * @param ctx Context of the activation. Literally any {@link Object }
     * @return {@code true} if ability activated successfully. {@code false} to cancel ability activation
     * @see #tryActivate(Player, Object...)
     */
    protected abstract boolean onActivation(Player player, Object... ctx);
}