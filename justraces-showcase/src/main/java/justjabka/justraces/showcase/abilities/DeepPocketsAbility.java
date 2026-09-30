package justjabka.justraces.showcase.abilities;

import justjabka.justraces.api.abilities.generic.BaseAbility;
import justjabka.justraces.api.abilities.AbilityContext;
import justjabka.justraces.api.abilities.AbilityTrigger;
import justjabka.justraces.api.abilities.generic.ConfigurableAbility;
import justjabka.justraces.showcase.JustRacesShowcase;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.key.Key;
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
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class DeepPocketsAbility extends BaseAbility implements ConfigurableAbility {

    private static final NamespacedKey INVENTORY = new NamespacedKey(JustRacesShowcase.NAMESPACE, "inventory");

    @Override
    public @NonNull NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "deep_pockets");
    }

    @Override
    public long cooldown() {
        return getConfigCooldown();
    }

    @Override
    public Component barIcon(Player player) {
        return Component.text("\uE006").font(Key.key(JustRacesShowcase.NAMESPACE, "cooldown_bar"));
    }

    @Override
    public BossBar.Color barColor(Player player) {
        return BossBar.Color.YELLOW;
    }

    @Override
    public List<Component> description() {
        return List.of(Component.text("Access an additional inventory, that keeps your items completely safe even through death."));
    }

    @Override
    public AbilityTrigger trigger() {
        return getConfigTrigger();
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

        ItemStack[] contents = getEntryInventory(player, INVENTORY);
        inventory.getInventory().setContents(contents);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;

        if (!(event.getInventory().getHolder(false) instanceof DeepPocketsAbilityInventory inventory)) return;

        ItemStack[] contents = inventory.getInventory().getContents();
        setEntryInventory(player, INVENTORY, contents);
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
        public @NonNull Inventory getInventory() {
            return this.inventory;
        }
    }
}
