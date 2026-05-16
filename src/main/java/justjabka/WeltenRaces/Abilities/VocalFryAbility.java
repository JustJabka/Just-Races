package justjabka.WeltenRaces.Abilities;

import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.Configs.Ability.VocalFryAbilityConfig;
import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.Types.Race;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Set;

import static justjabka.WeltenRaces.Abilities.TrueFormAbility.TRUE_FORM_KEY;

public class VocalFryAbility extends BaseAbility {
    private final VocalFryAbilityConfig config;
    private final Set<PotionEffect> targetDebuffs;

    public VocalFryAbility(VocalFryAbilityConfig config) {
        this.config = config;
        this.targetDebuffs = Set.of(
                new PotionEffect(PotionEffectType.SLOWNESS, config.duration, 1, false, true, true),
                new PotionEffect(PotionEffectType.WEAKNESS, config.duration, 1, false, true, true)
        );
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(WeltenRaces.NAMESPACE, "vocal_fry");
    }

    @Override
    public long getCooldownTicks() {
        return config.cooldown;
    }

    @EventHandler
    public void handleEntityInteract(PlayerInteractEntityEvent event) {
        super.handleEntityInteract(event);
    }

    @Override
    protected boolean onActivation(Player player, Object... ctx) {
        if (!AbilityManager.isAbilityActive(player, TRUE_FORM_KEY)) return false;

        if (ctx.length == 0) return false;
        if (!(ctx[0] instanceof LivingEntity target)) return false;

        int foodLevel = player.getFoodLevel();

        if (foodLevel <= config.foodRequired) return false;
        if (target instanceof Player targetPlayer && RaceManager.getRace(targetPlayer) == Race.LIZARD) return false;

        targetDebuffs.forEach(target::addPotionEffect);
        player.setFoodLevel(Math.max(0, foodLevel - config.foodDrained));

        return true;
    }
}
