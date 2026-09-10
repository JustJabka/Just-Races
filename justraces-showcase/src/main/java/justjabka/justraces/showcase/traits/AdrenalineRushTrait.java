package justjabka.justraces.showcase.traits;

import justjabka.justraces.api.interfaces.configurable.TraitConfigurable;
import justjabka.justraces.api.runnables.generic.BaseTraitRunnable;
import justjabka.justraces.showcase.JustRacesShowcase;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

public class AdrenalineRushTrait extends BaseTraitRunnable implements TraitConfigurable {

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "adrenaline_rush");
    }

    @Override
    public long getTickPeriod() {
        return 5;
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

        final float healthThresholdPercent = getConfigFloat("health_threshold_percent");
        final float speedBonusPerStack = getConfigFloat("speed_bonus_per_stack");

        int step = (int) (lostHealthPercent * healthThresholdPercent);

        return step * speedBonusPerStack;
    }

    private void applyAdrenalineRushBonus(Player player, double bonus) {
        AttributeInstance movementSpeedInstance = player.getAttribute(Attribute.MOVEMENT_SPEED);
        if (movementSpeedInstance == null) return;

        AttributeModifier modifier = movementSpeedInstance.getModifier(getKey());
        double currentBonus = modifier != null ? modifier.getAmount() : 0;

        if (currentBonus == bonus) return;

        movementSpeedInstance.removeModifier(getKey());

        if (bonus <= 0) return;

        movementSpeedInstance.addModifier(new AttributeModifier(
                getKey(),
                bonus,
                AttributeModifier.Operation.ADD_NUMBER
        ));
    }
}
