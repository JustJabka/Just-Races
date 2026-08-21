package justjabka.JustRacesShowcase.Abilities;

import justjabka.JustRaces.Abilities.Generic.BaseAbility;
import justjabka.JustRacesShowcase.Configs.Ability.PredatorVisionAbilityConfig;
import justjabka.JustRacesShowcase.JustRacesShowcase;
import justjabka.JustRaces.Managers.EffectManager;
import justjabka.JustRaces.Types.AbilityActivateAction;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class PredatorVisionAbility extends BaseAbility {
    PredatorVisionAbilityConfig config;

    public PredatorVisionAbility(PredatorVisionAbilityConfig config) {
        this.config = config;
    }

    List<LivingEntity> markedVictims = new ArrayList<>();

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "predator_vision");
    }

    @Override
    public long getCooldownTicks() {
        return config.cooldown;
    }

    @EventHandler
    public void handleInteract(PlayerInteractEvent event) {
        super.handleInteract(event);
    }

    @Override
    protected boolean onActivation(Player player, Object... ctx) {
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
        Bukkit.getScheduler().runTaskLater(JustRacesShowcase.INSTANCE, () -> {
            if (!player.isOnline()) return;

            for (LivingEntity victim : markedVictims) {
                if (!victim.isValid()) continue;

                EffectManager.setClientSideGlow(player, victim, false);
            }
        }, config.effectDuration);

        return true;
    }

    @Override
    protected boolean interactionAction(PlayerInteractEvent event, Player player) {
        return AbilityActivateAction.SHIFT_RIGHT_CLICK.check(event, player);
    }
}
