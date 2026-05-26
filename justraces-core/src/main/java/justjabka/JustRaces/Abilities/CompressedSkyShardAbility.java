package justjabka.JustRaces.Abilities;

import io.papermc.paper.datacomponent.DataComponentTypes;
import justjabka.JustRaces.Abilities.Generic.BaseAbility;
import justjabka.JustRaces.Configs.Ability.CompressedSkyShardAbilityConfig;
import justjabka.JustRaces.JustRacesAPI;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.RayTraceResult;

import java.util.List;

public class CompressedSkyShardAbility extends BaseAbility {
    private final CompressedSkyShardAbilityConfig config;

    public CompressedSkyShardAbility(CompressedSkyShardAbilityConfig config) {
        this.config = config;
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesAPI.NAMESPACE, "compressed_sky_shard");
    }

    @Override
    public long getCooldownTicks() {
        return config.cooldown;
    }

    @Override
    protected boolean canActivate(Player player) {
        Material activationItem = Material.POTION;
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();

        return itemInMainHand.getType() == activationItem;
    }

    @EventHandler
    public void handleInteract(PlayerInteractEvent event) {
        super.handleInteract(event);
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    protected boolean onActivation(Player player, Object... ctx) {
        ItemStack item = player.getInventory().getItemInMainHand();

        List<PotionEffect> potionEffects = item.getData(DataComponentTypes.POTION_CONTENTS).allEffects();
        if (potionEffects.isEmpty()) return false;

        RayTraceResult raycast = player.getWorld().rayTraceBlocks(
                player.getEyeLocation(),
                player.getEyeLocation().getDirection(),
                config.radius,
                FluidCollisionMode.NEVER,
                true
        );

        if (raycast == null) return false;
        if (raycast.getHitBlock() == null) return false;

        Location hitLocation = raycast.getHitBlock().getLocation().add(0, 1, 0);

        hitLocation.getWorld().spawn(
                hitLocation,
                AreaEffectCloud.class,
                CreatureSpawnEvent.SpawnReason.CUSTOM,
                cloud -> {
                    cloud.setRadius(config.effectRadius);
                    cloud.setDuration(config.effectDuration);
                    cloud.setSource(player);
                    for (PotionEffect effect : potionEffects) {
                        cloud.addCustomEffect(effect.withDuration(config.effectDuration), true);
                    }
                }
        );

        boolean hasInfiniteResources = player.getGameMode().isInvulnerable();
        if (!hasInfiniteResources) item.subtract();

        return true;
    }
}