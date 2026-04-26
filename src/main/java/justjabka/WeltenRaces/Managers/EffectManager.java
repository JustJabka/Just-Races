package justjabka.WeltenRaces.Managers;

import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@SuppressWarnings("UnstableApiUsage")
public class EffectManager {
    public static double calcAbsorptionAmountFromConsumable(Consumable consumable) {
        for (ConsumeEffect effect : consumable.consumeEffects()) {
            if (!(effect instanceof ConsumeEffect.ApplyStatusEffects applyEffect)) continue;
            for (PotionEffect potionEffect : applyEffect.effects()) {
                if (!(potionEffect.getType().equals(PotionEffectType.ABSORPTION))) continue;
                return (potionEffect.getAmplifier() + 1) * 4.0;
            }
        }
        return 0;
    }
}
