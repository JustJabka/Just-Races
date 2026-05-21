package justjabka.WeltenRaces.Runnables.Race;

import justjabka.WeltenRaces.Configs.Race.EpiphyteRaceConfig;
import justjabka.WeltenRaces.DataProvider.RaceProvider;
import justjabka.WeltenRaces.Runnables.Generic.BaseRaceRunnable;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class EpiphyteRaceRunnable extends BaseRaceRunnable {
    private final EpiphyteRaceConfig config;

    public EpiphyteRaceRunnable(EpiphyteRaceConfig config) {
        this.config = config;
    }

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
            AttributeModifier modifier = new AttributeModifier(
                    getRaceKey(),
                    config.mossMovementSpeedBonus,
                    AttributeModifier.Operation.ADD_NUMBER
            );

            movementSpeedInstance.addModifier(modifier);
        } else if (removeBuff) {
            movementSpeedInstance.removeModifier(getRaceKey());
        }
    }
}
