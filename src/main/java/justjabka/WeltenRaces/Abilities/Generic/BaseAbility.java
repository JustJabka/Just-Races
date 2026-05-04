package justjabka.WeltenRaces.Abilities.Generic;

import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.Types.Race;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class BaseAbility implements Listener {
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    protected static final TextColor ABILITY_ON_COOLDOWN_COLOR = TextColor.fromHexString("#a42431");
    protected static final TextColor ABILITY_READY_COLOR = TextColor.fromHexString("#79a049");

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
        String regex = "(\\p{Lu})";
        String replacement = " $1";

        String formattedName = name.replaceAll(regex, replacement).trim();
        return Component.text(formattedName);
    }

    public Component getAbilityDisplay(Player player) {
        Component displayName = getDisplayName();
        long remainingTime = getRemainingSeconds(player);

        Component abilityOnCooldownMessage = Component
                .translatable("ability.base.cooldown_message")
                .fallback("%s: %s")
                .arguments(displayName, Component.text(remainingTime))
                .color(ABILITY_ON_COOLDOWN_COLOR);

        Component abilityReadyMessage = Component
                .translatable("ability.base.ready_message")
                .fallback("%s")
                .arguments(displayName)
                .color(ABILITY_READY_COLOR)
                .decorate(TextDecoration.UNDERLINED);

        if (remainingTime > 0) return abilityOnCooldownMessage;
        return abilityReadyMessage;
    }

    // Ability activation
    protected boolean canActivate(Player player) {
        return true;
    }
    // TODO: add auto deactivation

    public void handleInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Race race = RaceManager.getRace(player);

        // Check abilities of race
        List<BaseAbility> allowedAbilities = AbilityManager.getAbilitiesForRace(race);
        if (!allowedAbilities.contains(this)) return;

        // Check activate conditions
        if (!canActivate(player)) return;

        if (!AbilityManager.hasActivationSlotSelected(player)) return;
        if (!activateAction(event, player)) return;

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

    protected boolean activateAction(PlayerInteractEvent event, Player player) {
        if (event.getHand() == EquipmentSlot.OFF_HAND) return false;
        if (!event.getAction().isRightClick()) return false;
        if (player.isSneaking()) return false;

        return true;
    }
}