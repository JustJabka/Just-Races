package justjabka.WeltenRaces.Abilities.Generic;

import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.Types.AbilityActivateAction;
import justjabka.WeltenRaces.Types.Race;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.*;

public abstract class BaseAbility implements Listener {
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    protected static final TextColor ABILITY_ON_COOLDOWN_COLOR = TextColor.fromHexString("#a42431");
    protected static final TextColor ABILITY_READY_COLOR = TextColor.fromHexString("#79a049");

    public abstract NamespacedKey getKey();
    public abstract long getCooldownTicks();


    public void setCooldownTicks(Player player, long newCooldown) {
        cooldowns.put(player.getUniqueId(), newCooldown);
    }

    public long getRemainingTicks(Player player) {
        long remaining = getExpireStamp(player) - getGameTime();
        return Math.max(0, remaining);
    }

    public long getRemainingSeconds(Player player) {
        return getRemainingTicks(player) / 20;
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
     * @see #getCooldownTicks()
     */
    public void putOnCooldown(Player player) {
        long expiresAt = getGameTime() + getCooldownTicks();
        cooldowns.put(player.getUniqueId(), expiresAt);
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
    private static long getGameTime() {
        return Bukkit.getWorlds().getFirst().getGameTime();
    }

    /**
     * Check if player's race has this ability
     * @param player Player that will be checked
     * @return {@code true} if player has this ability
     */
    public boolean raceHasAbility(Player player) {
        Race race = RaceManager.getRace(player);
        Set<BaseAbility> allowedAbilities = AbilityManager.getAbilitiesForRace(race);
        return allowedAbilities.contains(this);
    }

    /**
     * Gets ability display name as the text component
     * @return Component with translate and fallback
     * @see #getAbilityDisplay(Player)
     */
    public Component getDisplayName() {
        String name = this.getClass().getSimpleName();

        // Regex go brrrrr😎
        String removedSuffix = name.replaceFirst("Ability$", "");
        String snakeCase = removedSuffix.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();

        String translate = String.format("ability.%s.name", snakeCase);
        String fallback = removedSuffix.replaceAll("(\\p{Lu})", " $1").trim();

        return Component.translatable(translate).fallback(fallback);
    }

    /**
     * Gets ability display as the text component. That can be almost anything.
     * From basic ability name and cooldown to a very specific stats
     * @param player Player from which we get the ability display
     * @return Ability display as component
     * @apiNote Use {@code Component.empty()} to hide the ability display
     * @see #getDisplayName()
     */
    public Component getAbilityDisplay(Player player) {
        Component displayName = getDisplayName();
        long remainingTime = getRemainingSeconds(player);

        Component abilityOnCooldownMessage = Component
                .translatable("ability.base.cooldown_display")
                .fallback("%s: %s")
                .arguments(displayName, Component.text(remainingTime))
                .color(ABILITY_ON_COOLDOWN_COLOR);

        Component abilityReadyMessage = Component
                .translatable("ability.base.ready_display")
                .fallback("%s")
                .arguments(displayName)
                .color(ABILITY_READY_COLOR)
                .decorate(TextDecoration.UNDERLINED);

        if (remainingTime > 0) return abilityOnCooldownMessage;
        return abilityReadyMessage;
    }

    // Handlers
    public void handleInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!interactionAction(event, player)) return;
        tryActivate(player);
    }

    public void handleToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        if (!toggleSneakAction(event, player)) return;
        tryActivate(player);
    }

    public void handleEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity victim)) return;
        if (!(event.getDamager() instanceof Player attacker)) return;

        tryActivate(attacker, victim);
    }

    public void handleEntityInteract(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof LivingEntity clickedEntity)) return;
        Player player = event.getPlayer();

        tryActivate(player, clickedEntity);
    }

    // Actions
    protected boolean interactionAction(PlayerInteractEvent event, Player player) {
        return AbilityActivateAction.RIGHT_CLICK.check(event, player);
    }

    protected boolean toggleSneakAction(PlayerToggleSneakEvent event, Player player) {
        return event.isSneaking();
    }

    // Ability activation
    protected boolean canActivate(Player player) {
        return true;
    }

    protected void tryActivate(Player player, Object... ctx) {
        if (!raceHasAbility(player)) return;

        if (!canActivate(player)) return;
        if (!AbilityManager.hasActivationSlotSelected(player)) return;

        if (isOnCooldown(player)) return;

        if (!onActivation(player, ctx)) return;
        putOnCooldown(player);
    }

    protected abstract boolean onActivation(Player player, Object... ctx);
}