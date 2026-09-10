package justjabka.justraces.showcase.runnables.race;

import justjabka.justraces.api.interfaces.configurable.RaceConfigurable;
import justjabka.justraces.api.runnables.generic.BaseRaceRunnable;
import justjabka.justraces.showcase.dataprovider.RaceProvider;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BuzzlingRaceRunnable extends BaseRaceRunnable implements RaceConfigurable {
    private static final Random RANDOM = new Random();

    @Override
    public NamespacedKey getKey() {
        return RaceProvider.BUZZLING;
    }

    @Override
    public void onTick(Player player) {
        Location location = player.getLocation();

        int flowerCount = 0;
        Map<Location, Ageable> cropsNearby = new HashMap<>();

        final int searchRadiusX = getConfigInt("crop_pollinator", "radius", "x");
        final int searchRadiusY = getConfigInt("crop_pollinator", "radius", "y");
        final int searchRadiusZ = getConfigInt("crop_pollinator", "radius", "z");

        for (int x = -searchRadiusX; x <= searchRadiusX; x++) {
            for (int y = -searchRadiusY; y <= searchRadiusY; y++) {
                for (int z = -searchRadiusZ; z <= searchRadiusZ; z++) {
                    Block block = location.getBlock().getRelative(x, y, z);
                    Material blockType = block.getType();

                    if (Tag.FLOWERS.isTagged(blockType))  {
                        flowerCount++;
                    } else if (Tag.CROPS.isTagged(blockType) || Tag.SAPLINGS.isTagged(blockType)) {
                        if (!(block.getBlockData() instanceof Ageable ageable)) continue;
                        if (ageable.getAge() >= ageable.getMaximumAge()) continue;

                        cropsNearby.put(block.getLocation(), ageable);
                    }
                }
            }
        }

        final int minFlowerAmount = getConfigInt("crop_pollinator", "min_flower_amount");
        if (flowerCount < minFlowerAmount) return;

        final float chancePerFlower = getConfigFloat("crop_pollinator", "chance", "per_flower");
        final float maxChance = getConfigFloat("crop_pollinator", "chance", "max");
        float chance = Math.min(flowerCount * chancePerFlower, maxChance);

        spawnNectarParticle(player);

        pollinateCrops(chance, cropsNearby);
    }

    private void pollinateCrops(float chance, Map<Location, Ageable> cropsNearby) {
        if (cropsNearby.isEmpty()) return;

        cropsNearby.forEach((loc, ageable) -> {
            if (RANDOM.nextDouble() > chance) return;

            Block block = loc.getBlock();
            int maximumAge = ageable.getMaximumAge();
            int age = ageable.getAge();

            ageable.setAge(Math.min(age + 1, maximumAge));
            block.setBlockData(ageable);
        });
    }

    private void spawnNectarParticle(Player player) {
        Location loc = player.getLocation();

        Vector direction = loc.getDirection().setY(0).normalize();
        Vector backward = direction.clone().multiply(-0.35);

        Location particleLoc = loc.clone().add(0, 0.5, 0).add(backward);

        player.getWorld().spawnParticle(
                Particle.FALLING_NECTAR,
                particleLoc,
                10,
                0.05,
                0.05,
                0.05,
                0
        );
    }
}
