package justjabka.WeltenRaces.Abilities;

import justjabka.WeltenRaces.Abilities.Generic.BaseValidationAbility;
import justjabka.WeltenRaces.Configs.Ability.SwiftSneakAbilityConfig;
import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.Managers.AttributeManager;
import justjabka.WeltenRaces.Runnables.Ability.SwiftSneakAbilityRunnable;
import justjabka.WeltenRaces.Types.AbilityActivateAction;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.Set;

public class SwiftSneakAbility extends BaseValidationAbility {
    private final SwiftSneakAbilityConfig config;
    private final Map<Attribute, AttributeModifier> fastSneakAttributes;

    private static final Set<PotionEffect> fastSneakEffects = Set.of(
            new PotionEffect(PotionEffectType.SPEED, PotionEffect.INFINITE_DURATION, 0, false, false, false),
            new PotionEffect(PotionEffectType.INVISIBILITY, PotionEffect.INFINITE_DURATION, 0, false, false, false)
    );

    public SwiftSneakAbility(SwiftSneakAbilityConfig config) {
        this.config = config;
        this.fastSneakAttributes = Map.of(
                Attribute.SNEAKING_SPEED, new AttributeModifier(
                        getKey(),
                        config.sneakSpeedBonus,
                        AttributeModifier.Operation.ADD_NUMBER
                )
        );
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(WeltenRaces.NAMESPACE, "swift_sneak");
    }

    @Override
    public long getCooldownTicks() {
        return config.cooldown;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!AbilityManager.isAbilityActive(player, getKey())) return;
        clearFastSneak(player);
    }

    @EventHandler
    public void handleInteract(PlayerInteractEvent event) {
        super.handleInteract(event);
    }

    @Override
    protected boolean onActivation(Player player, Object... ctx) {
        if (AbilityManager.isAbilityActive(player, getKey())) return false;

        giveFastSneak(player);
        return true;
    }

    @Override
    public void onDeactivation(Player player) {
        clearFastSneak(player);
    }

    public void giveFastSneak(Player player) {
        AbilityManager.setAbilityState(player, getKey(), true);

        new SwiftSneakAbilityRunnable(this, player.getUniqueId()).runTaskTimer(WeltenRaces.INSTANCE, 0, 2);

        // Add bonuses
        fastSneakEffects.forEach(player::addPotionEffect);
        AttributeManager.addModifiers(player, fastSneakAttributes);
    }

    public void clearFastSneak(Player player) {
        AbilityManager.setAbilityState(player, getKey(), false);

        // Remove bonuses
        fastSneakEffects.forEach(effect -> player.removePotionEffect(effect.getType()));
        AttributeManager.removeModifiers(player, fastSneakAttributes);
    }

    @Override
    public boolean isStateValid(Player player) {
        return player.isSneaking();
    }

    @Override
    protected boolean interactionAction(PlayerInteractEvent event, Player player) {
        return AbilityActivateAction.SHIFT_LEFT_CLICK.check(event, player);
    }
}
