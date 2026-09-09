package justjabka.JustRacesShowcase.Abilities;

import justjabka.JustRaces.Abilities.Generic.BaseAbility;
import justjabka.JustRaces.Types.AbilityContext;
import justjabka.JustRaces.Types.Trigger;
import justjabka.JustRacesShowcase.JustRacesShowcase;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class DeepPocketsAbility extends BaseAbility {

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "deep_pockets");
    }

    @Override
    public long getCooldownTicks() {
        return 0;
    }

    @Override
    public Trigger getDefaultTrigger() {
        return Trigger.RIGHT_CLICK_CHESTPLATE;
    }

    @Override
    protected boolean onActivation(Player player, AbilityContext ctx) {
        Bukkit.getScheduler().runTask(JustRacesShowcase.INSTANCE, () -> {
            DeepPocketsAbilityInventory inventory = new DeepPocketsAbilityInventory(JustRacesShowcase.INSTANCE);
            player.openInventory(inventory.getInventory());
        });
        return true;
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;

        if (!(event.getInventory().getHolder(false) instanceof DeepPocketsAbilityInventory inventory)) return;

        ItemStack[] contents = getContainerInventory(player, getKey());
        inventory.getInventory().setContents(contents);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;

        if (!(event.getInventory().getHolder(false) instanceof DeepPocketsAbilityInventory inventory)) return;

        ItemStack[] contents = inventory.getInventory().getContents();
        setContainerInventory(player, getKey(), contents);
    }

    public static class DeepPocketsAbilityInventory implements InventoryHolder {
        private final Inventory inventory;

        public DeepPocketsAbilityInventory(Plugin plugin) {
            this.inventory = plugin.getServer().createInventory(
                    this,
                    9,
                    Component.translatable("container.deep_pockets", "Deep Pockets")
            );
        }

        @Override
        public @NotNull Inventory getInventory() {
            return this.inventory;
        }
    }
}
