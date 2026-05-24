package justjabka.JustRaces.Abilities;

import justjabka.JustRaces.Abilities.Generic.BaseAbility;
import justjabka.JustRaces.Configs.Ability.PoisonousBiteAbilityConfig;
import justjabka.JustRaces.JustRaces;
import justjabka.JustRaces.Managers.AbilityManager;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static justjabka.JustRaces.Abilities.TrueFormAbility.TRUE_FORM_KEY;

public class PoisonousBiteAbility extends BaseAbility {
    private final PoisonousBiteAbilityConfig config;
    private final PotionEffect victimDebuff;

    public PoisonousBiteAbility(PoisonousBiteAbilityConfig config) {
        this.config = config;
        this.victimDebuff = new PotionEffect(PotionEffectType.POISON, config.duration, 0, false, true, true);
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRaces.NAMESPACE, "poisonous_bite");
    }

    @Override
    public Component getAbilityDisplay(Player player) {
        return Component.empty();
    }

    @Override
    public long getCooldownTicks() {
        return config.cooldown;
    }

    @EventHandler
    public void handleEntityDamageByEntity(EntityDamageByEntityEvent event) {
        super.handleEntityDamageByEntity(event);
    }

    @Override
    protected boolean onActivation(Player attacker, Object... ctx) {
        if (ctx.length == 0) return false;
        if (!(ctx[0] instanceof LivingEntity victim)) return false;

        victim.addPotionEffect(victimDebuff);
        attacker.setFoodLevel(attacker.getFoodLevel() + config.foodBonus);

        return true;
    }

    @Override
    protected boolean canActivate(Player player) {
        return AbilityManager.isAbilityActive(player, TRUE_FORM_KEY);
    }
}
