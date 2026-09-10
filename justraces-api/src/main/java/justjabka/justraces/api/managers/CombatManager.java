package justjabka.justraces.api.managers;

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
    /**
     * Default cosine threshold for the blocking angle check.
     * <p>
     * Corresponds to roughly 78.5° on each side of the player's look direction,
     * forming the effective shield cone. A higher (less negative) value widens the cone.
     */
    public static final double DEFAULT_BLOCKING_ANGLE = -0.2;

    /**
     * Checks whether the target is actively blocking <b>toward</b> the source of the given {@link Location}.
     * <p>
     * This is <b>not</b> a simple {@link Player#isBlocking()} check. It resolves the damage
     * source location (falling back to the target's own location if unavailable) and then
     * verifies that the target's look direction falls within the shield's angular cone
     * relative to that source.
     * <p>
     * Uses the {@value #DEFAULT_BLOCKING_ANGLE} threshold.
     *
     * @param target The player who may be blocking
     * @param damageLocation Attack direction
     * @return {@code true} if the target is blocking <i>and</i> facing the damage source within the shield cone
     * @see #isFacingAndBlocking(Player, DamageSource)
     */
    public static boolean isFacingAndBlocking(Player target, Location damageLocation) {
        return isFacingAndBlocking(target, damageLocation, DEFAULT_BLOCKING_ANGLE);
    }

    /**
     * Checks whether the target is actively blocking <b>toward</b> the source of the given {@link DamageSource}.
     * <p>
     * This is <b>not</b> a simple {@link Player#isBlocking()} check. It resolves the damage
     * source location (falling back to the target's own location if unavailable) and then
     * verifies that the target's look direction falls within the shield's angular cone
     * relative to that source.
     * <p>
     * Uses the {@value #DEFAULT_BLOCKING_ANGLE} threshold.
     *
     * @param target The player who may be blocking
     * @param damageSource The damage source to derive the attack direction from
     * @return {@code true} if the target is blocking <i>and</i> facing the damage source within the shield cone
     * @see #isFacingAndBlocking(Player, Location)
     */
    public static boolean isFacingAndBlocking(Player target, DamageSource damageSource) {
        Location sourceLocation = damageSource.getSourceLocation();
        Location damageLocation = sourceLocation != null ? sourceLocation : target.getLocation();

        return isFacingAndBlocking(target, damageLocation, DEFAULT_BLOCKING_ANGLE);
    }

    /**
     * Checks whether the target is actively blocking <b>toward</b> the given damage location,
     * using a custom angular threshold.
     * <p>
     * This is <b>not</b> a simple {@link Player#isBlocking()} check. It performs a full
     * directional validation:
     * <ol>
     *   <li>Verifies the player is currently blocking ({@link Player#isBlocking()}).</li>
     *   <li>Computes the dot product between the target's look direction and the
     *       normalized vector from {@code damageLocation} to the target.</li>
     *   <li>Compares the result against {@code maxAngleCos} to determine if the
     *       shield is facing the attack within the allowed cone.</li>
     * </ol>
     * <p>
     * A lower (more negative) {@code maxAngleCos} widens the effective blocking cone;
     * a higher (more positive) value narrows it. The default is
     * {@value #DEFAULT_BLOCKING_ANGLE} (≈ 78.5° half-cone).
     *
     * @param target The player who may be blocking
     * @param damageLocation The location from which the damage originates
     * @param maxAngleCos Cosine of the maximum allowed angle between the target's look
     *                    direction and the vector pointing from the damage source to the target.
     *                    The block is valid when the dot product is strictly less than this value.
     * @return {@code true} if the target is blocking <i>and</i> the shield is facing
     *         the damage source within the specified angular threshold
     * @see #isFacingAndBlocking(Player, Location)
     */
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
