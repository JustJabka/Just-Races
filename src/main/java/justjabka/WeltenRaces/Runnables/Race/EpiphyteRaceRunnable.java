package justjabka.WeltenRaces.Runnables.Race;

import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.Types.Race;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class EpiphyteRaceRunnable extends BukkitRunnable {
    private static final NamespacedKey PARASITIC_NATURE_KEY = new NamespacedKey(WeltenRaces.NAMESPACE, "parasitic_nature");

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (RaceManager.getRace(player) != Race.EPIPHYTE) return;

            applyParasiticNatureBonus(player);
        }
    }

    private static void applyParasiticNatureBonus(Player player) {
        AttributeInstance movementSpeedInstance = player.getAttribute(Attribute.MOVEMENT_SPEED);
        if (movementSpeedInstance == null) return;

        Block steppingOn = player.getLocation().subtract(0, 1, 0).getBlock();

        boolean steppingOnBuffBlock = steppingOn.getType() == Material.MOSS_BLOCK;
        boolean hasModifier = movementSpeedInstance.getModifier(PARASITIC_NATURE_KEY) != null;

        boolean giveBuff = steppingOnBuffBlock && !hasModifier;
        boolean removeBuff = !steppingOnBuffBlock && hasModifier;

        if (giveBuff) {
            AttributeModifier modifier = new AttributeModifier(
                    PARASITIC_NATURE_KEY,
                    0.1,
                    AttributeModifier.Operation.ADD_NUMBER
            );

            movementSpeedInstance.addModifier(modifier);
        } else if (removeBuff) {
            movementSpeedInstance.removeModifier(PARASITIC_NATURE_KEY);
        }
    }
}
