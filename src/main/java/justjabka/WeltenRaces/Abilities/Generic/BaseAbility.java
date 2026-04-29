package justjabka.WeltenRaces.Abilities.Generic;

import justjabka.WeltenRaces.Managers.AbilityManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class BaseAbility implements Listener {
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    public abstract long getCooldownTicks();
    public abstract String getDisplayName();

    protected abstract boolean canActivate(Player player);

    public void handleInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!canActivate(player)) return;

        if (!AbilityManager.hasActivationSlotSelected(player)) return;
        if (!activateAction(event, player)) return;

        long gameTime = Bukkit.getWorlds().getFirst().getGameTime();
        long expireStamp = cooldowns.getOrDefault(player.getUniqueId(), 0L);

        boolean onCooldown = gameTime < expireStamp;
        if (onCooldown) {
            long remainingTicks = expireStamp - gameTime;
            long remainingSeconds = remainingTicks / 20;

            player.sendMessage(Component.text("%s is on cooldown wait %s sec.".formatted(getDisplayName(), remainingSeconds)));
            return;
        }

        if (onActivation(player)) {
            long expiresAt = gameTime + getCooldownTicks();
            cooldowns.put(player.getUniqueId(), expiresAt);
        }
    }

    protected abstract boolean onActivation(Player player);

    protected boolean activateAction(PlayerInteractEvent event, Player player) {
        if (event.getHand() == EquipmentSlot.OFF_HAND) return false;
        if (!event.getAction().isRightClick()) return false;

        return true;
    }
}