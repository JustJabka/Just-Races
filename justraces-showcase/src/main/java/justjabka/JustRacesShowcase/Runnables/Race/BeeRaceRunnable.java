package justjabka.JustRacesShowcase.Runnables.Race;

import justjabka.JustRaces.Runnables.Generic.BaseRaceRunnable;
import justjabka.JustRacesShowcase.DataProvider.RaceProvider;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BeeRaceRunnable extends BaseRaceRunnable {
    private static final Random RANDOM = new Random();

    // TODO: add config
    private static final float CHANCE_PER_FLOWER = 0.01f;
    private static final float MAX_CHANCE = 0.3f;

    private static final int REQUIRED_FLOWER_COUNT = 4;

    private static final int SEARCH_RADIUS_X = 9;
    private static final int SEARCH_RADIUS_Y = 2;
    private static final int SEARCH_RADIUS_Z = 9;

    @Override
    public NamespacedKey getKey() {
        return RaceProvider.BEE;
    }

    @Override
    public void onTick(Player player) {
        Location location = player.getLocation();

        int flowerCount = 0;
        Map<Location, Ageable> cropsNearby = new HashMap<>();

        for (int x = -SEARCH_RADIUS_X; x <= SEARCH_RADIUS_X; x++) {
            for (int y = -SEARCH_RADIUS_Y; y <= SEARCH_RADIUS_Y; y++) {
                for (int z = -SEARCH_RADIUS_Z; z <= SEARCH_RADIUS_Z; z++) {
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

        if (flowerCount < REQUIRED_FLOWER_COUNT) return;

        spawnNectarParticle(player);

        if (cropsNearby.isEmpty()) return;
        float chance = Math.min(flowerCount * CHANCE_PER_FLOWER, MAX_CHANCE);

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
