package justjabka.WeltenRaces.Abilities;

import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.Configs.Ability.DamageInversionAbilityConfig;
import justjabka.WeltenRaces.DataProvider.DamageTypeTagKeysProvider;
import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.Managers.ArmorManager;
import justjabka.WeltenRaces.Types.AbilityActivateAction;
import justjabka.WeltenRaces.Types.ArmorSet;
import justjabka.WeltenRaces.WeltenRaces;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
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
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class DamageInversionAbility extends BaseAbility {
    private final DamageInversionAbilityConfig config;

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
    public Component getAbilityDisplay(Player player) {
        boolean isActive = AbilityManager.isAbilityActive(player, getKey());

        Component displayName = getDisplayName();
        TextColor displayColor = isActive ? ABILITY_READY_COLOR : ABILITY_ON_COOLDOWN_COLOR;

        return Component
                .translatable("ability.damage_inversion.state")
                .fallback("%s")
                .arguments(displayName)
                .color(displayColor)
                .decorate(TextDecoration.UNDERLINED);
    }

    @Override
    protected boolean canActivate(Player player) {
        return ArmorManager.getArmorSet(player) == ArmorSet.LEATHER;
    }

    @EventHandler
    public void handleInteract(PlayerInteractEvent event) {
        super.handleInteract(event);
    }

    @Override
    protected boolean onActivation(Player player) {
        PersistentDataContainer abilities = AbilityManager.getAbilities(player);

        boolean currentState = AbilityManager.isAbilityActive(player, getKey());
        abilities.set(getKey(), PersistentDataType.BOOLEAN, !currentState);

        AbilityManager.updateAbilities(player, abilities);

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

        return true;
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!AbilityManager.isAbilityActive(player, getKey())) return;
        if (!canActivate(player)) return; // Temp fix! TODO: add auto deactivation

        double damage = event.getDamage();
        DamageSource damageSource = event.getDamageSource();
        DamageType damageType = damageSource.getDamageType();

        boolean canBypassInversion = DamageTypeTagKeysProvider.getTagValues(DamageTypeTagKeysProvider.BYPASSES_DAMAGE_INVERSION).contains(damageType);

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