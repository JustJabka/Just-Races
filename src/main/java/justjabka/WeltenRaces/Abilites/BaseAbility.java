package justjabka.WeltenRaces.Abilites;

import justjabka.WeltenRaces.Managers.AbilityManager;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

abstract class BaseAbility {
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    public abstract long getCooldownTicks();
    public abstract String getDisplayName();

    public void handleInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!AbilityManager.hasActivationSlotSelected(player)) return;
        if (!activateAction(event, player)) return;

        long gameTime = player.getWorld().getGameTime();
        long expiresAt = cooldowns.getOrDefault(player.getUniqueId(), 0L);

        boolean onCooldown = gameTime < expiresAt;
        if (onCooldown) {
            long remainingTicks = expiresAt - gameTime;
            long remainingSeconds = remainingTicks / 20;

            player.sendMessage(Component.text("%s is on cooldown wait %s sec.".formatted(getDisplayName(), remainingSeconds)));
            return;
        }

        if (onActivation(player)) { // Повертає true, якщо магія відбулася
            cooldowns.put(player.getUniqueId(), gameTime + getCooldownTicks());
        }
    }

    protected abstract boolean onActivation(Player player);

    protected boolean activateAction(PlayerInteractEvent event, Player player) {
        return event.getAction().isRightClick();
    }
}