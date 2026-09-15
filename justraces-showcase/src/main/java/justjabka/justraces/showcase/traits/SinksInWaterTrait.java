package justjabka.justraces.showcase.traits;

import justjabka.justraces.api.managers.ArmorManager;
import justjabka.justraces.api.traits.generic.BaseTraitRunnable;
import justjabka.justraces.api.traits.generic.ConfigurableTrait;
import justjabka.justraces.api.traits.generic.ResettableTrait;
import justjabka.justraces.showcase.JustRacesShowcase;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SinksInWaterTrait extends BaseTraitRunnable implements ResettableTrait, ConfigurableTrait {

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "sinks_in_water");
    }

    @Override
    public long getTickPeriod() {
        return 5;
    }

    @Override
    public void onTick(Player player) {
        applyWaterSink(player);
    }

    @Override
    public void applyState(Player player) {
        applyWaterSink(player);
    }

    @Override
    public void resetState(UUID pid) {
        Player player = Bukkit.getPlayer(pid);
        if (player == null) return;

        AttributeInstance gravityInstance = player.getAttribute(Attribute.GRAVITY);
        if (gravityInstance == null) return;

        gravityInstance.removeModifier(getKey());
    }

    private void applyWaterSink(Player player) {
        AttributeInstance gravityInstance = player.getAttribute(Attribute.GRAVITY);
        if (gravityInstance == null) return;

        boolean shouldSink = player.isInWater() && ArmorManager.hasAnyArmor(player);
        boolean hasModifier = gravityInstance.getModifier(getKey()) != null;

        boolean giveModifier = shouldSink && !hasModifier;
        boolean clearModifier = !shouldSink && hasModifier;

        if (giveModifier) {
            double sinkGravity = getConfigDouble("sink_gravity_value");

            AttributeModifier modifier = new AttributeModifier(
                    getKey(),
                    sinkGravity,
                    AttributeModifier.Operation.ADD_NUMBER
            );

            gravityInstance.addModifier(modifier);
        } else if (clearModifier) {
            gravityInstance.removeModifier(getKey());
        }
    }
}
