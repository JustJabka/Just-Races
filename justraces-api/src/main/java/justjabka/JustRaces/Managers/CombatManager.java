package justjabka.JustRaces.Managers;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.BlocksAttacks;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class CombatManager {
    public static final double DEFAULT_BLOCKING_ANGLE = -0.2;

    public static boolean isFacingAndBlocking(Player target, Location damageLocation) {
        return isFacingAndBlocking(target, damageLocation, DEFAULT_BLOCKING_ANGLE);
    }

    public static boolean isFacingAndBlocking(Player target, DamageSource damageSource) {
        Location sourceLocation = damageSource.getSourceLocation();
        Location damageLocation = sourceLocation != null ? sourceLocation : target.getLocation();

        return isFacingAndBlocking(target, damageLocation, DEFAULT_BLOCKING_ANGLE);
    }

    public static boolean isFacingAndBlocking(Player target, Location damageLocation, double maxAngleCos) {
        if (!target.isBlocking()) {
            return false;
        }

        Location targetLocation = target.getLocation();

        Vector attackerToTarget = targetLocation.toVector().subtract(damageLocation.toVector()).normalize();
        Vector targetLook = targetLocation.getDirection().normalize();

        return targetLook.dot(attackerToTarget) < maxAngleCos;
    }

    /**
     * Tries to disable player's block (shields or other stuff) with an attack
     * @param player Player whose block need to be broken
     * @param disableTicks Ticks for which block would be disabled (automatically scaled with disableCooldownScale)
     * @param damage Damage amount
     * @param damageSource Damage source
     * @return {@code true} if block was broken. {@code false} if there were no block
     * @apiNote If attack wasn't blocked it still goes into the player.
     * <p>
     * If {@code disabledTicks} is {@code 0} or less the player's block wouldn't be disabled, but it still would be damaged
     */
    public static boolean attackAndTryDisableBlock(Player player, int disableTicks, double damage, DamageSource damageSource) {
        if (!isFacingAndBlocking(player, damageSource)) {
            player.damage(damage, damageSource);
            return false;
        }

        // Get item and components
        ItemStack item = player.getActiveItem();
        BlocksAttacks blocksAttacks = item.getData(DataComponentTypes.BLOCKS_ATTACKS);
        if (blocksAttacks == null) {
            player.damage(damage, damageSource);
            return false;
        }

        // Play sound and damage item
        player.damage(damage, damageSource);

        // Calc cooldown
        float scale = blocksAttacks.disableCooldownScale();
        int cooldown = (int) (disableTicks * scale);

        // Disable item
        if (cooldown > 0) {
            // Put item on cooldown
            player.clearActiveItem();
            player.setCooldown(item, cooldown);

            // Play disable sound
            Key disableSoundKey = blocksAttacks.disableSound();
            if (disableSoundKey != null) player.getWorld().playSound(player.getLocation(), disableSoundKey.asString(), SoundCategory.PLAYERS, 1f, 1f);
        }

        return true;
    }
}
