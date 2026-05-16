package justjabka.WeltenRaces.Abilities;

import io.papermc.paper.event.entity.EntityEquipmentChangedEvent;
import justjabka.WeltenRaces.Abilities.Generic.BaseTogglableAbility;
import justjabka.WeltenRaces.Configs.Ability.DamageInversionAbilityConfig;
import justjabka.WeltenRaces.DataProvider.DamageTypeTagKeysProvider;
import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.Managers.ArmorManager;
import justjabka.WeltenRaces.Types.AbilityActivateAction;
import justjabka.WeltenRaces.Types.ArmorSet;
import justjabka.WeltenRaces.WeltenRaces;
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
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Collection;

public class DamageInversionAbility extends BaseTogglableAbility {
    private final DamageInversionAbilityConfig config;
    private static final Collection<DamageType> bypassesDamageInversion = DamageTypeTagKeysProvider.getTagValues(DamageTypeTagKeysProvider.BYPASSES_DAMAGE_INVERSION);

    public DamageInversionAbility(DamageInversionAbilityConfig config) {
        this.config = config;
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(WeltenRaces.NAMESPACE, "damage_inversion");
    }

    @Override
    public long getCooldownTicks() {
        return config.cooldown;
    }

    @Override
    protected boolean canActivate(Player player) {
        return ArmorManager.getArmorSet(player) == ArmorSet.LEATHER;
    }

    @Override
    public boolean isStateValid(Player player) {
        return ArmorManager.getArmorSet(player) == ArmorSet.LEATHER;
    }

    @EventHandler
    public void onArmorChange(EntityEquipmentChangedEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (!AbilityManager.isAbilityActive(player, getKey())) return;
        if (isStateValid(player)) return;

        disable(player);
    }

    @EventHandler
    public void handleInteract(PlayerInteractEvent event) {
        super.handleInteract(event);
    }

    @Override
    protected boolean onActivation(Player player, Object... ctx) {
        toggle(player);
        return true;
    }

    @Override
    public void onDeactivation(Player player) {
        disable(player);
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

        Range<Double> damageBoundary = Range.between(config.lowerBound, config.upperBound);
        boolean damageInBoundary = damageBoundary.contains(damage);

        if (!damageInBoundary) return;

        double finalDamage = (config.upperBound + config.lowerBound) - damage;
        event.setDamage(finalDamage);
    }

    @Override
    protected boolean interactionAction(PlayerInteractEvent event, Player player) {
        return AbilityActivateAction.SHIFT_RIGHT_CLICK.check(event, player);
    }
}