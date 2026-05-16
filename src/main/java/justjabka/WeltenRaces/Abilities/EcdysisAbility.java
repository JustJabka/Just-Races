package justjabka.WeltenRaces.Abilities;

import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.Configs.Ability.EcdysisAbilityConfig;
import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.Managers.ArmorManager;
import justjabka.WeltenRaces.Types.AbilityActivateAction;
import justjabka.WeltenRaces.Types.ArmorSet;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Set;
import java.util.UUID;

public class EcdysisAbility extends BaseAbility {
    private final EcdysisAbilityConfig config;

    private final Set<PotionEffect> userEffects;
    private final Set<PotionEffect> victimEffects;

    public EcdysisAbility(EcdysisAbilityConfig config) {
        this.config = config;
        this.userEffects = Set.of(
                new PotionEffect(PotionEffectType.RESISTANCE, config.effectDuration, 4, false, true),
                new PotionEffect(PotionEffectType.SPEED, 3 * 20, 3, false, true)
        );
        this.victimEffects = Set.of(
                new PotionEffect(PotionEffectType.BLINDNESS, config.effectDuration, 0, false, false)
        );
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(WeltenRaces.NAMESPACE, "ecdysis");
    }

    @Override
    public long getCooldownTicks() {
        return config.cooldown;
    }

    @Override
    protected boolean canActivate(Player player) {
        return ArmorManager.getArmorSet(player) == ArmorSet.NETHERITE;
    }

    @EventHandler
    public void handleInteract(PlayerInteractEvent event) {
        super.handleInteract(event);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        killPlayer(player);
    }

    @Override
    protected boolean onActivation(Player player, Object... ctx) {
        double avrgDurability = ArmorManager.getAverageDurability(player);
        boolean isSuicideUse = avrgDurability <= config.suicideDurabilityPercent;

        // Store if this is suicide use for later
        AbilityManager.changeAbilityState(player, getKey(), isSuicideUse);

        // Change durability
        for (ItemStack armor : player.getEquipment().getArmorContents()) {
            if (armor == null) continue;
            if (!(armor.getItemMeta() instanceof Damageable meta)) continue;

            int maxDamage = meta.hasMaxDamage()
                    ? meta.getMaxDamage()
                    : armor.getType().getMaxDurability();

            meta.setDamage(maxDamage - config.durabilityAfterUse);
            armor.setItemMeta(meta);
        }

        // Potion effects
        userEffects.forEach(player::addPotionEffect);

        for (LivingEntity victim : player.getLocation().getNearbyLivingEntities(10)) {
            if (victim == player) continue;
            victimEffects.forEach(victim::addPotionEffect);
        }

        onUseEffects(player);

        handleSuicideUse(player, isSuicideUse);

        return true;
    }

    private void handleSuicideUse(Player player, boolean isSuicideUse) {
        if (!isSuicideUse) return;

        final UUID pid = player.getUniqueId();

        Bukkit.getScheduler().runTaskLater(WeltenRaces.INSTANCE, () -> { // PREPARE THYSELF!
            Player suicidePlayer = Bukkit.getPlayer(pid);

            if (suicidePlayer == null) return;

            killPlayer(suicidePlayer);
        }, config.effectDuration);
    }

    private void killPlayer(Player suicidePlayer) {
        if (!AbilityManager.isAbilityActive(suicidePlayer, getKey())) return;

        // Prevent double death
        AbilityManager.changeAbilityState(suicidePlayer, getKey(), false);

        // DIE!
        suicidePlayer.setHealth(0);
    }

    private static void onUseEffects(Player player) {
        World world = player.getWorld();
        Location location = player.getLocation();

        world.spawnParticle(
                Particle.BLOCK,
                location.add(0, 1, 0),
                100,
                2.5,
                1,
                2.5,
                Bukkit.createBlockData(Material.NETHERITE_BLOCK)
        );
        world.spawnParticle(
                Particle.EXPLOSION_EMITTER,
                location.add(0, 1, 0),
                1,
                0,
                0,
                0,
                1
        );

        world.playSound(location, Sound.ENTITY_WITHER_BREAK_BLOCK, SoundCategory.PLAYERS, 1, 2);
        world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 2, 1);
    }

    @Override
    protected boolean interactionAction(PlayerInteractEvent event, Player player) {
        return AbilityActivateAction.SHIFT_RIGHT_CLICK.check(event, player);
    }
}