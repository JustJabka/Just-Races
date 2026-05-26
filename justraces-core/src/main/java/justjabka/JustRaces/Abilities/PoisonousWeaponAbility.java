package justjabka.JustRaces.Abilities;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import justjabka.JustRaces.Abilities.Generic.BaseAbility;
import justjabka.JustRaces.Configs.Ability.PoisonousWeaponAbilityConfig;
import justjabka.JustRaces.DataProvider.EnchantmentProvider;
import justjabka.JustRaces.JustRacesAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PoisonousWeaponAbility extends BaseAbility {
    private final PoisonousWeaponAbilityConfig config;
    private static final Material activationItem = Material.SPORE_BLOSSOM;

    public PoisonousWeaponAbility(PoisonousWeaponAbilityConfig config) {
        this.config = config;
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesAPI.NAMESPACE, "poisonous_weapon");
    }

    @Override
    public long getCooldownTicks() {
        return config.cooldown;
    }

    @Override
    public Component getAbilityDisplay(Player player) {
        return Component.empty();
    }

    @EventHandler
    public void handleInteract(PlayerInteractEvent event) {
        super.handleInteract(event);
    }

    @Override
    protected boolean canActivate(Player player) {
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack offhand = player.getInventory().getItemInOffHand();

        if (mainHand.isEmpty() || offhand.isEmpty()) return false;

        if (offhand.getType() != activationItem) return false;
        if (offhand.getAmount() < config.activationAmount) return false;

        if (mainHand.containsEnchantment(EnchantmentProvider.POISON)) return false;
        return EnchantmentProvider.POISON.canEnchantItem(mainHand);
    }

    @Override
    protected boolean onActivation(Player player, Object... ctx) {
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack offHand = player.getInventory().getItemInOffHand();

        mainHand.addEnchantment(EnchantmentProvider.POISON, 1);
        addLore(player, mainHand);

        offHand.subtract(config.activationAmount);

        onUseEffects(player);

        return true;
    }

    @SuppressWarnings("UnstableApiUsage")
    private static void addLore(Player player, ItemStack mainHand) {
        ItemLore lore = mainHand.getDataOrDefault(DataComponentTypes.LORE, ItemLore.lore().build());

        List<Component> newLore = new ArrayList<>(lore.lines());
        newLore.add(Component.text(player.getName()));

        mainHand.setData(DataComponentTypes.LORE, ItemLore.lore(newLore));
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
