package justjabka.JustRacesShowcase.Runnables.Race;

import justjabka.JustRacesShowcase.DataProvider.RaceProvider;
import justjabka.JustRaces.Managers.ArmorManager;
import justjabka.JustRaces.Runnables.Generic.BaseRaceRunnable;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

public class ArmatRaceRunnable extends BaseRaceRunnable {
    @Override
    public NamespacedKey getRaceKey() {
        return RaceProvider.ARMAT;
    }

    @Override
    public void onTick(Player player) {
        applyWaterSink(player);
    }

    private void applyWaterSink(Player player) {
        AttributeInstance gravityInstance = player.getAttribute(Attribute.GRAVITY);
        if (gravityInstance == null) return;

        boolean shouldSink = player.isInWater() && ArmorManager.hasAnyArmor(player);
        boolean hasModifier = gravityInstance.getModifier(getRaceKey()) != null;

        boolean giveModifier = shouldSink && !hasModifier;
        boolean clearModifier = !shouldSink && hasModifier;

        if (giveModifier) {
            double sinkGravity = getConfigDouble("sink_gravity_value");

            AttributeModifier modifier = new AttributeModifier(
                    getRaceKey(),
                    sinkGravity,
                    AttributeModifier.Operation.ADD_NUMBER
            );

            gravityInstance.addModifier(modifier);
        } else if (clearModifier) {
            gravityInstance.removeModifier(getRaceKey());
        }
    }
}
