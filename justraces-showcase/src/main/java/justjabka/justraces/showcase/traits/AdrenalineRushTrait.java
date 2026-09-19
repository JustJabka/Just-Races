package justjabka.justraces.showcase.traits;

import justjabka.justraces.api.managers.HealthManager;
import justjabka.justraces.api.traits.generic.ConfigurableTrait;
import justjabka.justraces.api.traits.generic.BaseTraitRunnable;
import justjabka.justraces.api.traits.generic.ResettableTrait;
import justjabka.justraces.showcase.JustRacesShowcase;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AdrenalineRushTrait extends BaseTraitRunnable implements ResettableTrait, ConfigurableTrait {

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

    @Override
    public void resetState(UUID pid, Reason reason) {
        Player player = Bukkit.getPlayer(pid);
        if (player == null) return;

        AttributeInstance movementSpeedInstance = player.getAttribute(Attribute.MOVEMENT_SPEED);
        if (movementSpeedInstance == null) return;

        movementSpeedInstance.removeModifier(getKey());
    }

    private double calcAdrenalineRushBonus(Player player) {
        double lostHealthPercent = HealthManager.getLostHealthPercent(player);

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
