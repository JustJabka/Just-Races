package justjabka.justraces.showcase.abilities;

import justjabka.justraces.api.abilities.generic.BaseAbility;
import justjabka.justraces.api.interfaces.configurable.AbilityConfigurable;
import justjabka.justraces.api.managers.CombatManager;
import justjabka.justraces.api.types.AbilityContext;
import justjabka.justraces.showcase.JustRacesShowcase;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PoisonousStingAbility extends BaseAbility implements AbilityConfigurable {

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "poisonous_sting");
    }

    @Override
    public long getCooldownTicks() {
        return getConfigCooldown();
    }

    @Override
    public BossBar.Color getCooldownBarColor(Player player) {
        return BossBar.Color.GREEN;
    }

    @Override
    public Component getCooldownBarIcon(Player player) {
        return Component.text("\uE005").font(Key.key(JustRacesShowcase.NAMESPACE, "cooldown_bar"));
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!(event.getDamager() instanceof LivingEntity attacker)) return;

        if (!playerHasAbility(player)) return;

        DamageSource damageSource = event.getDamageSource();
        if (damageSource.isIndirect()) return;
        if (CombatManager.isFacingAndBlocking(player, damageSource)) return;

        tryActivate(player, AbilityContext.ofAttacker(attacker));
    }

    @Override
    protected boolean onActivation(Player player, AbilityContext ctx) {
        final PotionEffect attackerEffect = new PotionEffect(
                PotionEffectType.POISON,
                getConfigInt("attacker_effect", "duration"),
                getConfigInt("attacker_effect", "amplifier"),
                false,
                true,
                true
        );

        ctx.attacker().ifPresent(attacker -> {
            attacker.addPotionEffect(attackerEffect);
            attacker.getWorld().playSound(attacker.getLocation(), Sound.ENTITY_BEE_STING, SoundCategory.PLAYERS, 1f, 1f);
        });
        return true;
    }
}
