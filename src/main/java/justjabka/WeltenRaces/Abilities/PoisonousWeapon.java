package justjabka.WeltenRaces.Abilities;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.keys.EnchantmentKeys;
import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.WeltenRaces;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PoisonousWeapon extends BaseAbility {
    private static final TypedKey<Enchantment> POISON_ENCHANTMENT_KEY =  EnchantmentKeys.create(Key.key(WeltenRaces.NAMESPACE, "poison"));
    private static final Registry<Enchantment> ENCHANTMENT_REGISTRY = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
    private static final Enchantment POISON_ENCHANTMENT = ENCHANTMENT_REGISTRY.getOrThrow(POISON_ENCHANTMENT_KEY);

    private static final Material ACTIVATION_ITEM = Material.SPORE_BLOSSOM;
    private static final int ACTIVATION_AMOUNT = 64;

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

        if (mainHand.containsEnchantment(POISON_ENCHANTMENT)) return false;
        return POISON_ENCHANTMENT.canEnchantItem(mainHand);
    }

    @Override
    protected boolean onActivation(Player player) {
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack offHand = player.getInventory().getItemInOffHand();

        mainHand.addEnchantment(POISON_ENCHANTMENT, 1);
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
