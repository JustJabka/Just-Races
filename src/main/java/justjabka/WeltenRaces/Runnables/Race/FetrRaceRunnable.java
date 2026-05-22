package justjabka.WeltenRaces.Runnables.Race;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.keys.tags.BiomeTagKeys;
import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.Managers.AttributeManager;
import justjabka.WeltenRaces.Runnables.Generic.BaseRaceRunnable;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static justjabka.WeltenRaces.DataProvider.RaceProvider.FETR;
import static justjabka.WeltenRaces.Listeners.Race.FetrRaceListener.isIdol;

@SuppressWarnings("UnstableApiUsage")
public class FetrRaceRunnable extends BaseRaceRunnable {
    public static final NamespacedKey FETR_STATUS_KEY = new NamespacedKey(WeltenRaces.NAMESPACE, "fetr_status");

    private static final int buffDuration = 11 * 20;

    private static final Registry<Biome> biomeRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME);
    private static final Collection<Biome> biomeBuffBiomes = biomeRegistry.getTagValues(BiomeTagKeys.IS_FOREST);
    private static final Set<PotionEffect> biomeBuffEffects = Set.of(
            new PotionEffect(PotionEffectType.RESISTANCE, buffDuration, 0, false, false, true),
            new PotionEffect(PotionEffectType.HASTE, buffDuration, 0, false, false, true)
    );

    private static final PotionEffect itemBuffEffect = new PotionEffect(
            PotionEffectType.HEALTH_BOOST,
            buffDuration,
            1,
            false,
            false,
            false
    );

    private static final Set<PotionEffect> headlinerStatusEffects = Set.of(
            new PotionEffect(PotionEffectType.SPEED, PotionEffect.INFINITE_DURATION, 1, false, false, false),
            new PotionEffect(PotionEffectType.JUMP_BOOST, PotionEffect.INFINITE_DURATION, 1, false, false, false),
            new PotionEffect(PotionEffectType.GLOWING, PotionEffect.INFINITE_DURATION, 0, false, false, false) // Good luck. Enjoy being killed first. The worst race of all
    );

    private final Map<Attribute, AttributeModifier> idolStatusModifiers = Map.of(
            Attribute.ATTACK_DAMAGE, new AttributeModifier(
                    getRaceKey(),
                    -0.8,
                    AttributeModifier.Operation.ADD_NUMBER
            ),
            Attribute.ENTITY_INTERACTION_RANGE, new AttributeModifier(
                    getRaceKey(),
                    1,
                    AttributeModifier.Operation.ADD_NUMBER
            ),
            Attribute.ATTACK_SPEED, new AttributeModifier(
                    getRaceKey(),
                    -3.5,
                    AttributeModifier.Operation.ADD_NUMBER
            )
    );

    @Override
    public NamespacedKey getRaceKey() {
        return FETR;
    }

    @Override
    public void onTick(Player player) {
        giveHornBuff(player);
        giveBiomeBuff(player);
        updateStatus(player);
    }

    private void giveHornBuff(Player player) {
        ItemStack item = player.getInventory().getItem(AbilityManager.getActivationSlot());

        if (item == null) return;
        if (item.isEmpty()) return;

        if (item.getType() != Material.GOAT_HORN) return;
        player.addPotionEffect(itemBuffEffect);
    }

    private void giveBiomeBuff(Player player) {
        Biome biome = player.getLocation().getBlock().getBiome();

        if (!biomeBuffBiomes.contains(biome)) return;
        biomeBuffEffects.forEach(player::addPotionEffect);
    }

    private void updateStatus(Player player) {
        double statusUpdateRadius = getConfig().node("status-update-radius").getDouble();

        Location location = player.getLocation();

        Collection<Player> playersNearby = location.getNearbyPlayers(statusUpdateRadius);
        boolean hasListeners = playersNearby.stream().anyMatch(nearby -> !nearby.equals(player));

        boolean wasIdol = isIdol(player);

        if (hasListeners) {
            giveIdolStatus(player, wasIdol);
        } else {
            giveHeadlinerStatus(player, wasIdol);
        }
    }

    private void giveIdolStatus(Player player, boolean wasIdol) {
        updateStatusValue(player, true);

        if (!wasIdol) {
            headlinerStatusEffects.forEach(effect -> player.removePotionEffect(effect.getType()));
            AttributeManager.addModifiers(player, idolStatusModifiers);
        }
    }

    private void giveHeadlinerStatus(Player player, boolean wasIdol) {
        updateStatusValue(player, false);

        if (wasIdol) {
            headlinerStatusEffects.forEach(player::addPotionEffect);
            AttributeManager.removeModifiers(player, idolStatusModifiers);
        }
    }

    private static void updateStatusValue(Player player, boolean isIdol) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        pdc.set(FETR_STATUS_KEY, PersistentDataType.BOOLEAN, isIdol);
    }
}
