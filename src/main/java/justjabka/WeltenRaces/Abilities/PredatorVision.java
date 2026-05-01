package justjabka.WeltenRaces.Abilities;

import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.Configs.Abilities.PredatorVisionConfig;
import justjabka.WeltenRaces.Managers.EffectManager;
import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.Types.Race;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.Bukkit;
import org.bukkit.Tag;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;
import java.util.List;

public class PredatorVision extends BaseAbility {
    PredatorVisionConfig config;

    public PredatorVision(PredatorVisionConfig config) {
        this.config = config;
    }

    List<LivingEntity> markedVictims = new ArrayList<>();

    @Override
    public long getCooldownTicks() {
        return config.cooldown;
    }

    @Override
    protected boolean canActivate(Player player) {
        return RaceManager.getRace(player) == Race.PHANTOM;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        super.handleInteract(event);
    }

    @Override
    protected boolean onActivation(Player player) {
        // Apply glow
        for (LivingEntity victim : player.getLocation().getNearbyLivingEntities(config.radius)) {
            if (victim == player) continue;

            EntityType victimType = victim.getType();
            boolean isUndead = Tag.ENTITY_TYPES_UNDEAD.getValues().contains(victimType);

            if (isUndead) continue;

            EffectManager.setClientSideGlow(player, victim, true);
            markedVictims.add(victim);
        }

        // Remove glow
        Bukkit.getScheduler().runTaskLater(WeltenRaces.INSTANCE, () -> {
            if (!player.isOnline()) return;

            for (LivingEntity victim : markedVictims) {
                if (!victim.isValid()) continue;

                EffectManager.setClientSideGlow(player, victim, false);
            }
        }, config.effectDuration);

        return true;
    }

    @Override
    protected boolean activateAction(PlayerInteractEvent event, Player player) {
        if (event.getHand() == EquipmentSlot.OFF_HAND) return false;
        if (!event.getAction().isRightClick()) return false;
        if (!player.isSneaking()) return false;

        return true;
    }
}
