package justjabka.JustRaces.Abilities;

import justjabka.JustRaces.Abilities.Generic.BaseAbility;
import justjabka.JustRaces.Configs.Ability.VocalFryAbilityConfig;
import justjabka.JustRaces.DataProvider.RaceProvider;
import justjabka.JustRaces.JustRacesAPI;
import justjabka.JustRaces.Managers.AbilityManager;
import justjabka.JustRaces.Managers.RaceManager;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Set;

import static justjabka.JustRaces.Abilities.TrueFormAbility.TRUE_FORM_KEY;

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
    public Component getAbilityDisplay(Player player) {
        if (!AbilityManager.isAbilityActive(player, TRUE_FORM_KEY)) return Component.empty();
        return super.getAbilityDisplay(player);
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesAPI.NAMESPACE, "vocal_fry");
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
        if (ctx.length == 0) return false;
        if (!(ctx[0] instanceof LivingEntity target)) return false;

        int foodLevel = player.getFoodLevel();

        if (foodLevel <= config.foodRequired) return false;
        if (target instanceof Player targetPlayer && RaceManager.isRace(targetPlayer, RaceProvider.LIZARD)) return false;

        targetDebuffs.forEach(target::addPotionEffect);
        player.setFoodLevel(Math.max(0, foodLevel - config.foodDrained));

        return true;
    }

    @Override
    protected boolean canActivate(Player player) {
        if (!AbilityManager.isAbilityActive(player, TRUE_FORM_KEY)) return false;
        return !player.isSneaking();
    }
}
