package justjabka.WeltenRaces.Abilities;

import justjabka.WeltenRaces.Abilities.Generic.BaseValidationAbility;
import justjabka.WeltenRaces.Configs.Ability.TrueFormAbilityConfig;
import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.Managers.AttributeManager;
import justjabka.WeltenRaces.Types.AbilityActivateAction;
import justjabka.WeltenRaces.WeltenRaces;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class TrueFormAbility extends BaseValidationAbility {
    public static final NamespacedKey TRUE_FORM_KEY = new NamespacedKey(WeltenRaces.NAMESPACE, "true_form");

    private final TrueFormAbilityConfig config;
    private static final int maxDuration = 90 * 20;

    private final Map<Attribute, AttributeModifier> trueFormModifiers;
    private final Set<PotionEffect> trueFormBuffs;
    private final Set<PotionEffect> trueFormDebuffs;

    private static final Map<UUID, Long> formExpireStamp = new HashMap<>();

    @Override
    public Component getAbilityDisplay(Player player) {
        if (AbilityManager.isAbilityActive(player, getKey())) return Component.empty();
        return super.getAbilityDisplay(player);
    }

    public TrueFormAbility(TrueFormAbilityConfig config) {
        this.config = config;

        this.trueFormModifiers = Map.of(
                Attribute.SCALE, new AttributeModifier(getKey(), config.scaleBonus, AttributeModifier.Operation.ADD_NUMBER),
                Attribute.MAX_HEALTH, new AttributeModifier(getKey(), config.maxHealthBonus, AttributeModifier.Operation.ADD_NUMBER)
        );
        this.trueFormBuffs = Set.of(
                new PotionEffect(PotionEffectType.RESISTANCE, PotionEffect.INFINITE_DURATION, 0, false, true, true),
                new PotionEffect(PotionEffectType.SPEED, PotionEffect.INFINITE_DURATION, 1, false, true, true)
        );
        this.trueFormDebuffs = Set.of(
                new PotionEffect(PotionEffectType.SLOWNESS, config.debuffDuration, 2, false, true, true),
                new PotionEffect(PotionEffectType.MINING_FATIGUE, config.debuffDuration, 1, false, true, true),
                new PotionEffect(PotionEffectType.WEAKNESS, config.debuffDuration, 1, false, true, true)
        );
    }

    @Override
    public NamespacedKey getKey() {
        return TRUE_FORM_KEY;
    }

    @Override
    public long getCooldownTicks() {
        return config.cooldown;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!AbilityManager.isAbilityActive(player, getKey())) return;
        clearTrueForm(player);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();

        if (!AbilityManager.isAbilityActive(player, getKey())) return;
        clearTrueForm(player);
    }

    @EventHandler
    public void handleInteract(PlayerInteractEvent event) {
        super.handleInteract(event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPotionEffect(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (!AbilityManager.isAbilityActive(player, getKey())) return;

        PotionEffect effect = event.getNewEffect();
        if (effect == null) return;

        if (effect.getType() != PotionEffectType.POISON) return;
        event.setCancelled(true);
    }

    @Override
    protected boolean onActivation(Player player, Object... ctx) {
        giveTrueForm(player);
        return true;
    }

    @Override
    public void onDeactivation(Player player) {
        clearTrueForm(player);
    }

    public void giveTrueForm(Player player) {
        AbilityManager.changeAbilityState(player, getKey(), true);

        trueFormBuffs.forEach(player::addPotionEffect);
        AttributeManager.addModifiers(player, trueFormModifiers);

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.PLAYERS, 1, 2);

        long expireStamp = getGameTime() + config.duration;
        formExpireStamp.put(player.getUniqueId(), expireStamp);

        scheduleFormCheck(player);
    }

    public void clearTrueForm(Player player) {
        AbilityManager.changeAbilityState(player, getKey(), false);

        trueFormBuffs.forEach(effect -> player.removePotionEffect(effect.getType()));
        trueFormDebuffs.forEach(player::addPotionEffect);
        AttributeManager.removeModifiers(player, trueFormModifiers);
    }

    public static void extendTrueForm(Player player, int seconds) {
        UUID pid = player.getUniqueId();
        if (!formExpireStamp.containsKey(pid)) return;

        long currentExpiry = formExpireStamp.get(pid);
        long currentTicks = getGameTime();

        long newExpireStamp = currentExpiry + (seconds * 20L);
        long maxAllowedExpireStamp = currentTicks + maxDuration;

        if (newExpireStamp > maxAllowedExpireStamp) {
            newExpireStamp = maxAllowedExpireStamp;
        }

        formExpireStamp.put(pid, newExpireStamp);
    }

    private void scheduleFormCheck(Player player) {
        Bukkit.getScheduler().runTaskLater(WeltenRaces.INSTANCE, () -> {
            if (!player.isOnline()) return;
            if (!AbilityManager.isAbilityActive(player, getKey())) return;

            UUID pid = player.getUniqueId();

            long currentTime = getGameTime();
            long expireStamp = formExpireStamp.getOrDefault(pid, 0L);

            if (currentTime < expireStamp) {
                scheduleFormCheck(player);
            } else {
                clearTrueForm(player);
                formExpireStamp.remove(pid);
            }
        }, 20L);
    }

    @Override
    protected boolean interactionAction(PlayerInteractEvent event, Player player) {
        return AbilityActivateAction.SHIFT_RIGHT_CLICK.check(event, player);
    }
}
