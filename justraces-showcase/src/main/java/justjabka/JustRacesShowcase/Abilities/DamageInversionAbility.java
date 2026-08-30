package justjabka.JustRacesShowcase.Abilities;

import io.papermc.paper.event.entity.EntityEquipmentChangedEvent;
import justjabka.JustRaces.Abilities.Generic.TogglableAbility;
import justjabka.JustRaces.Interfaces.Configurable.AbilityConfigurable;
import justjabka.JustRaces.Managers.AbilityManager;
import justjabka.JustRaces.Managers.ArmorManager;
import justjabka.JustRaces.Types.ArmorSet;
import justjabka.JustRacesShowcase.DataProvider.DamageTypeTagKeysProvider;
import justjabka.JustRacesShowcase.JustRacesShowcase;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.Range;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.Collection;

public class DamageInversionAbility extends TogglableAbility implements AbilityConfigurable {
    private static final Collection<DamageType> bypassesDamageInversion = DamageTypeTagKeysProvider.getTagValues(DamageTypeTagKeysProvider.BYPASSES_DAMAGE_INVERSION);

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "damage_inversion");
    }

    @Override
    public long getCooldownTicks() {
        return getConfigCooldown();
    }

    @Override
    public BossBar.Color getCooldownBarColor(Player player) {
        boolean isActive = AbilityManager.isAbilityActive(player, getKey());
        return isActive ? BossBar.Color.GREEN : BossBar.Color.RED;
    }

    @Override
    public Component getCooldownBarIcon(Player player) {
        return Component.text("\uE000").font(Key.key(JustRacesShowcase.NAMESPACE, "cooldown_bar"));
    }

    @Override
    protected boolean canActivate(Player player) {
        return ArmorManager.getArmorSet(player) == ArmorSet.LEATHER;
    }

    @Override
    public boolean isStateValid(Player player) {
        return ArmorManager.getArmorSet(player) == ArmorSet.LEATHER;
    }

    @EventHandler(ignoreCancelled = true)
    public void onArmorChange(EntityEquipmentChangedEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (!AbilityManager.isAbilityActive(player, getKey())) return;
        if (isStateValid(player)) return;

        disable(player);
    }

    @EventHandler(ignoreCancelled = true)
    public void trigger(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();

        if (!event.getMainHandItem().isEmpty()) return;
        if (!event.getOffHandItem().isEmpty()) return;

        if (!tryActivate(player)) return;
        event.setCancelled(true);
    }

    @Override
    protected boolean onActivation(Player player, Object... ctx) {
        toggle(player);
        return true;
    }

    @Override
    public void onToggle(Player player, boolean state) {
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_CANDLE_EXTINGUISH, SoundCategory.PLAYERS, 1, 2);
        player.getWorld().spawnParticle(
                Particle.CRIT,
                player.getEyeLocation().subtract(0, 0.5, 0),
                10,
                0.25,
                0.5,
                0.25,
                0.05
        );
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!AbilityManager.isAbilityActive(player, getKey())) return;

        double damage = event.getDamage();
        DamageSource damageSource = event.getDamageSource();
        DamageType damageType = damageSource.getDamageType();

        boolean canBypassInversion = bypassesDamageInversion.contains(damageType);

        if (canBypassInversion) return;

        double lowerBound = getConfigDouble("lower_bound");
        double upperBound = getConfigDouble("upper_bound");

        Range<Double> damageBoundary = Range.between(lowerBound, upperBound);
        boolean damageInBoundary = damageBoundary.contains(damage);

        if (!damageInBoundary) return;

        double finalDamage = (upperBound + lowerBound) - damage;
        event.setDamage(finalDamage);
    }
}