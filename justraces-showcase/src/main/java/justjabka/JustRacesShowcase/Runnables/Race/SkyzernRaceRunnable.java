package justjabka.JustRacesShowcase.Runnables.Race;

import justjabka.JustRacesShowcase.DataProvider.RaceProvider;
import justjabka.JustRaces.Runnables.Generic.BaseRaceRunnable;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

public class SkyzernRaceRunnable extends BaseRaceRunnable {
    @Override
    public NamespacedKey getRaceKey() {
        return RaceProvider.SKYZERN;
    }

    @Override
    public void onTick(Player player) {
        applyCelestialOriginBonus(player);
    }

    private void applyCelestialOriginBonus(Player player) {
        AttributeInstance attackDamageInstance = player.getAttribute(Attribute.ATTACK_DAMAGE);
        if (attackDamageInstance == null) return;

        double bonus = calcCelestialOriginBonus(player);

        // Delete old attribute
        attackDamageInstance.removeModifier(getRaceKey());

        // Apply new attribute
        AttributeModifier modifier = new AttributeModifier(
                getRaceKey(),
                bonus,
                AttributeModifier.Operation.ADD_NUMBER
        );

        attackDamageInstance.addModifier(modifier);
    }

    private double calcCelestialOriginBonus(Player player) {
        double damageBonusPerStep = getConfig().node("damage_bonus", "value_per_step").getDouble();
        double damageBonusStep = getConfig().node("damage_bonus", "step").getDouble();

        double currentHeight = player.getY();
        double baseHeight = player.getWorld().getSeaLevel();
        double step = damageBonusPerStep / damageBonusStep;

        return (currentHeight - baseHeight) * step;
    }
}
