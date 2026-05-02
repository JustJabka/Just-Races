package justjabka.WeltenRaces.Abilities.Generic;

import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.Types.Race;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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

    // Time
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

    // Display
    public String getDisplayName() {
        String name = this.getClass().getSimpleName();
        String regex = "(\\p{Lu})";
        String replacement = " $1";

        return name.replaceAll(regex, replacement).trim();
    }

    public Component getAbilityDisplay(Player player) {
        long remaining = getRemainingSeconds(player);

        if (remaining > 0) {
            return Component.text("%s: %s"
                    .formatted(getDisplayName(), remaining)
            ).color(NamedTextColor.RED);
        } else {
            return Component.text("%s"
                    .formatted(getDisplayName())
            ).color(NamedTextColor.GREEN).decorate(TextDecoration.UNDERLINED);
        }
    }

    // Activation and Interaction
    protected boolean canActivate(Player player) {
        return true;
    }

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