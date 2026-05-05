package justjabka.WeltenRaces.Abilities;

import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.Configs.Ability.WeightlessWillowSwayAbilityConfig;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Set;

public class WeightlessWillowSwayAbility extends BaseAbility {
    private final WeightlessWillowSwayAbilityConfig config;

    public WeightlessWillowSwayAbility(WeightlessWillowSwayAbilityConfig config) {
        this.config = config;
        this.USER_EFFECTS = Set.of(
                new PotionEffect(PotionEffectType.JUMP_BOOST, config.effectDuration, 3, false, true, true),
                new PotionEffect(PotionEffectType.SPEED, config.effectDuration, 0, false, true, true)
        );
    }

    private final Set<PotionEffect> USER_EFFECTS;

    @Override
    public long getCooldownTicks() {
        return config.cooldown;
    }

    @Override
    protected boolean canActivate(Player player) {
        Material activationItem = Material.FEATHER;
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();

        return itemInMainHand.getType() == activationItem;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        super.handleInteract(event);
    }

    @Override
    protected boolean onActivation(Player player) {
        USER_EFFECTS.forEach(player::addPotionEffect);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BREEZE_IDLE_AIR, SoundCategory.PLAYERS, 1, 1);
        return true;
    }
}
