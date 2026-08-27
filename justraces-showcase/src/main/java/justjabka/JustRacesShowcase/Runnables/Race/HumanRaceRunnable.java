package justjabka.JustRacesShowcase.Runnables.Race;

import justjabka.JustRaces.Runnables.Generic.BaseRaceRunnable;
import justjabka.JustRacesShowcase.DataProvider.RaceProvider;
import justjabka.JustRacesShowcase.JustRacesShowcase;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

public class HumanRaceRunnable extends BaseRaceRunnable {
    private static final NamespacedKey ADRENALINE_RUSH_KEY = new NamespacedKey(JustRacesShowcase.NAMESPACE, "adrenaline_rush");

    @Override
    public NamespacedKey getRaceKey() {
        return RaceProvider.HUMAN;
    }

    @Override
    public void onTick(Player player) {
        double speedBonusPerStack = calcAdrenalineRushBonus(player);
        applyAdrenalineRushBonus(player, speedBonusPerStack);
    }

    private double calcAdrenalineRushBonus(Player player) {
        AttributeInstance maxHealthInstance = player.getAttribute(Attribute.MAX_HEALTH);
        if (maxHealthInstance == null) return 0;

        double currentHealth = player.getHealth();
        double maxHealth = maxHealthInstance.getValue();
        double lostHealthPercent = (maxHealth - currentHealth) / maxHealth;

        final float healthThresholdPercent = getConfigFloat("adrenaline_rush", "health_threshold_percent");
        final float speedBonusPerStack = getConfigFloat("adrenaline_rush", "speed_bonus_per_stack");

        int step = (int) (lostHealthPercent * healthThresholdPercent);

        return step * speedBonusPerStack;
    }

    private void applyAdrenalineRushBonus(Player player, double bonus) {
        AttributeInstance movementSpeedInstance = player.getAttribute(Attribute.MOVEMENT_SPEED);
        if (movementSpeedInstance == null) return;

        AttributeModifier modifier = movementSpeedInstance.getModifier(ADRENALINE_RUSH_KEY);
        double currentBonus = modifier != null ? modifier.getAmount() : 0;

        if (currentBonus == bonus) return;

        movementSpeedInstance.removeModifier(ADRENALINE_RUSH_KEY);

        if (bonus <= 0) return;

        movementSpeedInstance.addModifier(new AttributeModifier(
                ADRENALINE_RUSH_KEY,
                bonus,
                AttributeModifier.Operation.ADD_NUMBER
        ));
    }
}
