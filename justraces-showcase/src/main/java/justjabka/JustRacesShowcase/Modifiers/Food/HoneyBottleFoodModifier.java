package justjabka.JustRacesShowcase.Modifiers.Food;

import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import io.papermc.paper.datacomponent.item.UseCooldown;
import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect;
import justjabka.JustRaces.Modifiers.Generic.BaseFoodModifier;
import justjabka.JustRacesShowcase.JustRacesShowcase;
import org.bukkit.NamespacedKey;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class HoneyBottleFoodModifier extends BaseFoodModifier {
    // TODO: add config
    private static final int SPEED_DURATION = 3 * 60 * 20;
    private static final int RESISTANCE_DURATION = 90 * 20;

    private static final List<PotionEffect> CONSUME_EFFECTS = List.of(
            new PotionEffect(PotionEffectType.RESISTANCE, RESISTANCE_DURATION, 0, false, true, true),
            new PotionEffect(PotionEffectType.HASTE, SPEED_DURATION, 0, false, true, true),
            new PotionEffect(PotionEffectType.SPEED, SPEED_DURATION, 0, false, true, true)
    );

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "food/honey_bottle");
    }

    @Override
    public Consumer<Consumable.Builder> getConsumable() {
        return builder -> builder
                .addEffect(ConsumeEffect.applyStatusEffects(
                    CONSUME_EFFECTS,
                    1f
                ))
                .hasConsumeParticles(false);
    }

    @Override
    public Consumer<FoodProperties.Builder> getFoodProperties() {
        return builder -> builder
                .nutrition(20)
                .saturation(20);
    }

    @Override
    public @Nullable UseCooldown getUseCooldown() {
        return null;
    }
}
