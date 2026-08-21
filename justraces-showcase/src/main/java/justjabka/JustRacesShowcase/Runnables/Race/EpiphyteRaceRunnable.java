package justjabka.JustRacesShowcase.Runnables.Race;

import justjabka.JustRacesShowcase.DataProvider.RaceProvider;
import justjabka.JustRaces.Runnables.Generic.BaseRaceRunnable;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class EpiphyteRaceRunnable extends BaseRaceRunnable {
    @Override
    public NamespacedKey getRaceKey() {
        return RaceProvider.EPIPHYTE;
    }

    @Override
    public void onTick(Player player) {
        applyMossSpeedBonus(player);
    }

    private void applyMossSpeedBonus(Player player) {
        AttributeInstance movementSpeedInstance = player.getAttribute(Attribute.MOVEMENT_SPEED);
        if (movementSpeedInstance == null) return;

        Block steppingOn = player.getLocation().subtract(0, 1, 0).getBlock();

        boolean steppingOnBuffBlock = steppingOn.getType() == Material.MOSS_BLOCK;
        boolean hasModifier = movementSpeedInstance.getModifier(getRaceKey()) != null;

        boolean giveBuff = steppingOnBuffBlock && !hasModifier;
        boolean removeBuff = !steppingOnBuffBlock && hasModifier;

        if (giveBuff) {
            double mossMovementSpeedBonus = getConfig().node("moss_movement_speed_bonus").getDouble();

            AttributeModifier modifier = new AttributeModifier(
                    getRaceKey(),
                    mossMovementSpeedBonus,
                    AttributeModifier.Operation.ADD_NUMBER
            );

            movementSpeedInstance.addModifier(modifier);
        } else if (removeBuff) {
            movementSpeedInstance.removeModifier(getRaceKey());
        }
    }
}
