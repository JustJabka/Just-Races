package justjabka.weltenRaces.Races.Armat;

import io.papermc.paper.event.entity.EntityEquipmentChangedEvent;
import justjabka.weltenRaces.Races.Generic.BaseRaceListener;
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
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Set;

public class ArmatRaceListener extends BaseRaceListener {
    private final ArmatConfig settings;

    public ArmatRaceListener(ArmatConfig settings) {
        this.settings = settings;
    }

    private static final NamespacedKey BOUND_SHELL_KEY = new NamespacedKey(WeltenRaces.PLUGIN_ID, "bound_shell");
    private static final NamespacedKey ARMOR_SET_KEY = new NamespacedKey(WeltenRaces.PLUGIN_ID, "armor_set");
    private static final NamespacedKey SINK_GRAVITY_KEY = new NamespacedKey(WeltenRaces.PLUGIN_ID, "sink_gravity");

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

        double damage = event.getDamage();
        EntityDamageEvent.DamageCause damageCause = event.getCause();

        // Damage vulnerability and immunity logic
        if (VULNERABLE_TO.contains(damageCause)) {
            event.setDamage(event.getDamage() * settings.vulnerableMultiplier);
            return;
        } else if (IMMUNE_TO.contains(damageCause) && hasAnyArmor(player)) {
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, SoundCategory.PLAYERS, 0.5f, 1.5f);
            event.setCancelled(true);
            return;
        }

        // Damage inversion logic
        // TODO: add toggle
        if (hasArmorSetBonus(player, "LEATHER")) {
            if (!(damage >= settings.inversionMin && damage <= settings.inversionMax)) return;

            double finalDamage = (settings.inversionMax + settings.inversionMin) - damage;

            event.setDamage(finalDamage);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!event.hasChangedBlock()) return;

        Player player = event.getPlayer();

        if (!raceEquals(player, "armat")) return;

        AttributeInstance gravityInstance = player.getAttribute(Attribute.GRAVITY);
        if (gravityInstance == null) return;

        boolean shouldSink = player.isInWater() && hasAnyArmor(player);
        boolean hasModifier = gravityInstance.getModifier(SINK_GRAVITY_KEY) != null;

        if (shouldSink && !hasModifier) {
            AttributeModifier modifier = new AttributeModifier(
                    SINK_GRAVITY_KEY,
                    settings.sinkGravity,
                    AttributeModifier.Operation.ADD_NUMBER
            );

            gravityInstance.addModifier(modifier);
        } else if (!shouldSink && hasModifier) {
            gravityInstance.removeModifier(SINK_GRAVITY_KEY);
        }
    }

    @EventHandler
    public void onArmorChange(EntityEquipmentChangedEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player player)) return;

        if (!raceEquals(player, "armat")) return;

        applyBoundShellBonus(player);
        updateArmorSetBonus(player);
    }

    @EventHandler
    public void onItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();

        if (!raceEquals(player, "armat")) return;

        if (!hasArmorSetBonus(player, "GOLDEN")) return;

        ItemStack consumedItem = event.getItem();

        Material goldenVersion = switch (consumedItem.getType()) {
            case APPLE -> Material.GOLDEN_APPLE;
            case CARROT -> Material.GOLDEN_CARROT;
            default -> null;
        };

        if (goldenVersion == null) return;

        ItemStack resultItem = new ItemStack(goldenVersion).asOne();

        event.setItem(resultItem);
        event.setReplacement(consumedItem.subtract());
    }

    private static void applyBoundShellBonus(Player player) {
        // Get Attributes
        AttributeInstance maxHealthInstance = player.getAttribute(Attribute.MAX_HEALTH);
        AttributeInstance armorInstance = player.getAttribute(Attribute.ARMOR);

        if (maxHealthInstance == null || armorInstance == null) return;

        double armorValue = armorInstance.getValue();

        // Delete old attribute
        maxHealthInstance.removeModifier(BOUND_SHELL_KEY);

        // Calc new attribute
        if (armorValue <= 0) return;

        AttributeModifier modifier = new AttributeModifier(
                BOUND_SHELL_KEY,
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