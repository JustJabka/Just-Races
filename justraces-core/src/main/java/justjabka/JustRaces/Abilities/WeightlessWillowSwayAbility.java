package justjabka.JustRaces.Abilities;

import justjabka.JustRaces.Abilities.Generic.BaseAbility;
import justjabka.JustRaces.Configs.Ability.WeightlessWillowSwayAbilityConfig;
import justjabka.JustRaces.JustRacesAPI;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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
    private final Set<PotionEffect> userEffects;

    public WeightlessWillowSwayAbility(WeightlessWillowSwayAbilityConfig config) {
        this.config = config;
        this.userEffects = Set.of(
                new PotionEffect(PotionEffectType.JUMP_BOOST, config.effectDuration, 3, false, true, true),
                new PotionEffect(PotionEffectType.SPEED, config.effectDuration, 0, false, true, true)
        );
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesAPI.NAMESPACE, "weightless_willow_sway");
    }

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
    public void handleInteract(PlayerInteractEvent event) {
        super.handleInteract(event);
    }

    @Override
    protected boolean onActivation(Player player, Object... ctx) {
        userEffects.forEach(player::addPotionEffect);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BREEZE_IDLE_AIR, SoundCategory.PLAYERS, 1, 1);
        return true;
    }
}
