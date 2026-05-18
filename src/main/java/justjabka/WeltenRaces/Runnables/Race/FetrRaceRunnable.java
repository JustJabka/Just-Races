package justjabka.WeltenRaces.Runnables.Race;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.keys.tags.BiomeTagKeys;
import justjabka.WeltenRaces.Managers.RaceManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.Set;

import static justjabka.WeltenRaces.DataProvider.RaceProvider.FETR;

@SuppressWarnings("UnstableApiUsage")
public class FetrRaceRunnable extends BukkitRunnable {
    private static final Registry<Biome> biomeRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME);
    private static final Collection<Biome> biomeBuffBiomes = biomeRegistry.getTagValues(BiomeTagKeys.IS_FOREST);
    private static final Set<PotionEffect> biomeBuffEffects = Set.of(
            new PotionEffect(PotionEffectType.RESISTANCE, 40, 0, false, false, true),
            new PotionEffect(PotionEffectType.HASTE, 40, 0, false, false, true)
    );

    private static final Material itemBuffItem = Material.GOAT_HORN;
    private static final PotionEffect itemBuffEffect = new PotionEffect(
            PotionEffectType.HEALTH_BOOST,
            11 * 20,
            1,
            false,
            false,
            true
    );

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!RaceManager.isRace(player, FETR)) return;

            giveHornBuff(player);
            giveBiomeBuff(player);
        }
    }

    private static void giveBiomeBuff(Player player) {
        Biome biome = player.getLocation().getBlock().getBiome();

        if (!biomeBuffBiomes.contains(biome)) return;
        biomeBuffEffects.forEach(player::addPotionEffect);
    }

    private static void giveHornBuff(Player player) {
        ItemStack item = player.getInventory().getItem(8);

        if (item == null) return;
        if (item.isEmpty()) return;

        if (item.getType() != itemBuffItem) return;
        player.addPotionEffect(itemBuffEffect);
    }
}
