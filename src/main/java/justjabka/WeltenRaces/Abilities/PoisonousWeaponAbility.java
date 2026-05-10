package justjabka.WeltenRaces.Abilities;

import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.DataProvider.EnchantmentProvider;
import justjabka.WeltenRaces.WeltenRaces;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PoisonousWeaponAbility extends BaseAbility {
    private static final Material ACTIVATION_ITEM = Material.SPORE_BLOSSOM;
    private static final int ACTIVATION_AMOUNT = 64;

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(WeltenRaces.NAMESPACE, "poisonous_weapon");
    }

    @Override
    public long getCooldownTicks() {
        return 20;
    }

    @Override
    public Component getAbilityDisplay(Player player) {
        return Component.empty();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        super.handleInteract(event);
    }

    @Override
    protected boolean canActivate(Player player) {
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack offhand = player.getInventory().getItemInOffHand();

        if (mainHand.isEmpty() || offhand.isEmpty()) return false;

        if (offhand.getType() != ACTIVATION_ITEM) return false;
        if (offhand.getAmount() < ACTIVATION_AMOUNT) return false;

        if (mainHand.containsEnchantment(EnchantmentProvider.POISON)) return false;
        return EnchantmentProvider.POISON.canEnchantItem(mainHand);
    }

    @Override
    protected boolean onActivation(Player player) {
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack offHand = player.getInventory().getItemInOffHand();

        mainHand.addEnchantment(EnchantmentProvider.POISON, 1);
        offHand.subtract(ACTIVATION_AMOUNT);

        onUseEffects(player);

        return true;
    }

    private static void onUseEffects(Player player) {
        World world = player.getWorld();
        Location location = new Location(
                world,
                player.getX(),
                player.getBoundingBox().getCenterY(),
                player.getZ()
        );

        world.spawnParticle(Particle.SPORE_BLOSSOM_AIR,
                location,
                5,
                0.25,
                0.5,
                0.25,
                0.1
        );
        world.spawnParticle(Particle.CHERRY_LEAVES,
                location,
                5,
                0.25,
                0.5,
                0.25,
                0.1
        );

        world.playSound(location, Sound.ITEM_BONE_MEAL_USE, SoundCategory.PLAYERS, 1, 1);
        world.playSound(location, Sound.BLOCK_BREWING_STAND_BREW, SoundCategory.PLAYERS, 0.5f, 1.5f);
    }
}
