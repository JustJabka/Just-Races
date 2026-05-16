package justjabka.WeltenRaces.Abilities;

import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.WeltenRaces;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static justjabka.WeltenRaces.Abilities.TrueFormAbility.TRUE_FORM_KEY;

public class PoisonousBiteAbility extends BaseAbility {
    private static final PotionEffect victimDebuff = new PotionEffect(PotionEffectType.POISON, 8 * 20, 0, false, true, true);
    private static final int foodBonus = 1;

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(WeltenRaces.NAMESPACE, "poisonous_bite");
    }

    @Override
    public Component getAbilityDisplay(Player player) {
        return Component.empty();
    }

    @Override
    public long getCooldownTicks() {
        return 40;
    }

    @EventHandler
    public void handleEntityDamageByEntity(EntityDamageByEntityEvent event) {
        super.handleEntityDamageByEntity(event);
    }

    @Override
    protected boolean onActivation(Player attacker, Object... ctx) {
        if (!AbilityManager.isAbilityActive(attacker, TRUE_FORM_KEY)) return false;

        if (ctx.length == 0) return false;
        if (!(ctx[0] instanceof LivingEntity victim)) return false;

        victim.addPotionEffect(victimDebuff);
        attacker.setFoodLevel(attacker.getFoodLevel() + foodBonus);

        return true;
    }
}
