package justjabka.WeltenRaces.Abilities;

import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.Managers.ArmorManager;
import justjabka.WeltenRaces.Types.ArmorSet;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class DamageInversion extends BaseAbility {
    public static final NamespacedKey DAMAGE_INVERSION_KEY = new NamespacedKey(WeltenRaces.NAMESPACE, "damage_inversion");

    @Override
    public long getCooldownTicks() {
        return 20;
    }

    @Override
    protected boolean canActivate(Player player) {
        return ArmorManager.getArmorSet(player) == ArmorSet.LEATHER;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        super.handleInteract(event);
    }

    @Override
    protected boolean onActivation(Player player) {
        PersistentDataContainer abilities = AbilityManager.getAbilities(player);

        boolean currentState = AbilityManager.isAbilityActive(player, DAMAGE_INVERSION_KEY);
        abilities.set(DAMAGE_INVERSION_KEY, PersistentDataType.BOOLEAN, !currentState);

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

    @Override
    protected boolean activateAction(PlayerInteractEvent event, Player player) {
        if (event.getHand() == EquipmentSlot.OFF_HAND) return false;
        if (!event.getAction().isRightClick()) return false;
        if (!player.isSneaking()) return false;

        return true;
    }
}