package justjabka.WeltenRaces.Abilites;

import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.Managers.ArmorManager;
import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.Types.ArmorSet;
import justjabka.WeltenRaces.Types.Race;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Set;
import java.util.UUID;

public class Ecdysis extends BaseAbility {
    public static final NamespacedKey ECDYSIS_KEY = new NamespacedKey(WeltenRaces.NAMESPACE, "ecdysis");

    private static final double suicidePercent = 0.15;
    private static final int effectDuration = 11 * 20;

    private static final Set<PotionEffect> USER_EFFECTS = Set.of(
            new PotionEffect(PotionEffectType.RESISTANCE, effectDuration, 4, false, true),
            new PotionEffect(PotionEffectType.SPEED, (2 * 20) + 20, 1, false, true)
    );
    private static final Set<PotionEffect> VICTIM_EFFECT = Set.of(
            new PotionEffect(PotionEffectType.BLINDNESS, effectDuration, 0, false, false)
    );

    @Override
    public long getCooldownTicks() {
        return 10 * 20;
    }

    @Override
    public String getDisplayName() {
        return "Ecdysis";
    }

    @Override
    protected boolean canActivate(Player player) {
        if (RaceManager.getRace(player) != Race.ARMAT) return false;
        if (ArmorManager.getArmorSet(player) != ArmorSet.NETHERITE) return false;

        return true;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        super.handleInteract(event);
    }

    @Override
    protected boolean onActivation(Player player) {
        double avrgDurability = ArmorManager.getAverageDurability(player);
        boolean isSuicideUse = avrgDurability <= suicidePercent;

        // Store if this is suicide use for later
        PersistentDataContainer abilities = AbilityManager.getAbilities(player);
        abilities.set(ECDYSIS_KEY, PersistentDataType.BOOLEAN, isSuicideUse);
        AbilityManager.updateAbilities(player, abilities);

        // Change durability
        for (ItemStack armor : player.getEquipment().getArmorContents()) {
            if (armor == null) continue;
            if (!(armor.getItemMeta() instanceof Damageable meta)) continue;

            int maxDamage = meta.hasMaxDamage()
                    ? meta.getMaxDamage()
                    : armor.getType().getMaxDurability();

            meta.setDamage(maxDamage - 1);
            armor.setItemMeta(meta);
        }

        // Potion effects
        USER_EFFECTS.forEach(player::addPotionEffect);

        for (LivingEntity victim : player.getLocation().getNearbyLivingEntities(10)) {
            if (victim == player) continue;
            VICTIM_EFFECT.forEach(victim::addPotionEffect);
        }

        onUseEffects(player);

        handleSuicideUse(player, isSuicideUse);

        return true;
    }

    private static void handleSuicideUse(Player player, boolean isSuicideUse) {
        if (!isSuicideUse) return;

        final UUID pid = player.getUniqueId();

        Bukkit.getScheduler().runTaskLater(WeltenRaces.INSTANCE, () -> { // PREPARE THYSELF!
            Player suicidePlayer = Bukkit.getPlayer(pid);

            if (suicidePlayer == null) return;

            // Prevent double death
            PersistentDataContainer currentAbilities = AbilityManager.getAbilities(suicidePlayer);
            if (!AbilityManager.isAbilityActive(suicidePlayer, ECDYSIS_KEY)) return;

            currentAbilities.set(ECDYSIS_KEY, PersistentDataType.BOOLEAN, false);
            AbilityManager.updateAbilities(suicidePlayer, currentAbilities);

            // DIE!
            suicidePlayer.setHealth(0);
        }, effectDuration);
    }

    private static void onUseEffects(Player player) {
        player.spawnParticle(
                Particle.BLOCK,
                player.getLocation().add(0, 1, 0),
                100,
                2.5,
                1,
                2.5,
                Bukkit.createBlockData(Material.NETHERITE_BLOCK)
        );
        player.spawnParticle(
                Particle.EXPLOSION_EMITTER,
                player.getLocation().add(0, 1, 0),
                1,
                0,
                0,
                0,
                1
        );

        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_BREAK_BLOCK, SoundCategory.PLAYERS, 1, 2);
        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 2, 1);
    }

    @Override
    protected boolean activateAction(PlayerInteractEvent event, Player player) {
        if (event.getHand() == EquipmentSlot.OFF_HAND) return false;
        if (!event.getAction().isRightClick()) return false;
        if (!player.isSneaking()) return false;

        return true;
    }
}