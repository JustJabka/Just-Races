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
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.*;

public abstract class BaseAbility implements Listener {
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    protected static final TextColor ABILITY_ON_COOLDOWN_COLOR = TextColor.fromHexString("#a42431");
    protected static final TextColor ABILITY_READY_COLOR = TextColor.fromHexString("#79a049");

    public abstract NamespacedKey getKey();

    // Ability time
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

    private Long getExpireStamp(Player player) {
        return cooldowns.getOrDefault(player.getUniqueId(), 0L);
    }

    private static long getGameTime() {
        return Bukkit.getWorlds().getFirst().getGameTime();
    }

    // Ability display
    public Component getDisplayName() {
        String name = this.getClass().getSimpleName();

        // Regex go brrrrr😎
        String removedSuffix = name.replaceFirst("Ability$", "");
        String snakeCase = removedSuffix.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();

        String translate = String.format("ability.%s.name", snakeCase);
        String fallback = removedSuffix.replaceAll("(\\p{Lu})", " $1").trim();

        return Component.translatable(translate).fallback(fallback);
    }

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

    protected void tryActivate(Player player) {
        Race race = RaceManager.getRace(player);

        // Check abilities of race
        Set<BaseAbility> allowedAbilities = AbilityManager.getAbilitiesForRace(race);
        if (!allowedAbilities.contains(this)) return;

        // Check activate conditions
        if (!canActivate(player)) return;

        if (!AbilityManager.hasActivationSlotSelected(player)) return;

        long gameTime = getGameTime();
        long expireStamp = getExpireStamp(player);

        // If on cooldown
        boolean onCooldown = gameTime < expireStamp;
        if (onCooldown) return;

        // On activation
        if (onActivation(player)) {
            long expiresAt = gameTime + getCooldownTicks();
            cooldowns.put(player.getUniqueId(), expiresAt);
        }
    }

    protected abstract boolean onActivation(Player player);
}