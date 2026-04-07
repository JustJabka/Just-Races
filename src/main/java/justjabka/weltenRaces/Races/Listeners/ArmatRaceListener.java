package justjabka.weltenRaces.Races.Listeners;

import io.papermc.paper.event.entity.EntityEquipmentChangedEvent;
import justjabka.weltenRaces.WeltenRaces;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Set;

public class ArmatRaceListener extends GenericRaceListener {
    private static final NamespacedKey BOUND_SHELL = new NamespacedKey(WeltenRaces.PLUGIN_ID, "bound_shell");
    private static final NamespacedKey ARMOR_SET_KEY = new NamespacedKey(WeltenRaces.PLUGIN_ID, "armor_set");

    private static final double VULNERABLE_DAMAGE_MULTIPLIER = 1.5;
    private static final Set<EntityDamageEvent.DamageCause> IMMUNE_TO = Set.of(
            EntityDamageEvent.DamageCause.FALL
    );
    private static final Set<EntityDamageEvent.DamageCause> VULNERABLE_TO = Set.of(
            EntityDamageEvent.DamageCause.MAGIC,
            EntityDamageEvent.DamageCause.POISON,
            EntityDamageEvent.DamageCause.WITHER,
            EntityDamageEvent.DamageCause.THORNS
    );
    
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player player)) return;

        if (!raceEquals(player, "armat")) return;

        EntityDamageEvent.DamageCause damageCause = event.getCause();

        if (VULNERABLE_TO.contains(damageCause)) {
            event.setDamage(event.getDamage() * VULNERABLE_DAMAGE_MULTIPLIER);
        } else if (IMMUNE_TO.contains(damageCause) && hasAnyArmor(player)) {
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, SoundCategory.PLAYERS, 0.5f, 1.5f);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerArmorChange(EntityEquipmentChangedEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player player)) return;

        if (!raceEquals(player, "armat")) return;

        applyBoundShellBonus(player);
        updateArmorSetBonus(player);
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();

        if (!raceEquals(player, "armat")) return;

        if (hasArmorSetBonus(player, "GOLDEN")) return;

        ItemStack item = event.getItem();

        Material goldenVersion = switch (item.getType()) {
            case APPLE -> Material.GOLDEN_APPLE;
            case CARROT -> Material.GOLDEN_CARROT;
            default -> null;
        };

        if (goldenVersion != null) event.setItem(ItemStack.of(goldenVersion));
    }

    private static void applyBoundShellBonus(Player player) {
        // Get Attributes
        AttributeInstance maxHealthInstance = player.getAttribute(Attribute.MAX_HEALTH);
        AttributeInstance armorInstance = player.getAttribute(Attribute.ARMOR);

        if (maxHealthInstance == null || armorInstance == null) return;

        double armorValue = armorInstance.getValue();

        // Delete old attribute
        maxHealthInstance.removeModifier(BOUND_SHELL);

        // Calc new attribute
        if (armorValue <= 0) return;

        AttributeModifier modifier = new AttributeModifier(
                BOUND_SHELL,
                armorValue,
                AttributeModifier.Operation.ADD_NUMBER
        );
        maxHealthInstance.addModifier(modifier);
    }

    private void updateArmorSetBonus(Player player) {
        ItemStack[] equipment = player.getEquipment().getArmorContents();
        String material = null;

        for (ItemStack item : equipment) {
            if (item == null || item.isEmpty()) {
                material = "NONE";
                break;
            }

            String currentMat = item.getType().name().split("_")[0];
            if (material == null) {
                material = currentMat;
            } else if (!material.equals(currentMat)) {
                material = "MIXED";
                break;
            }
        }

        player.getPersistentDataContainer().set(ARMOR_SET_KEY, PersistentDataType.STRING, material);
    }

    private static boolean hasArmorSetBonus(Player player, String material) {
        PersistentDataContainer data = player.getPersistentDataContainer();
        String armorSet = data.get(ARMOR_SET_KEY, PersistentDataType.STRING);

        return material.equals(armorSet);
    }
}