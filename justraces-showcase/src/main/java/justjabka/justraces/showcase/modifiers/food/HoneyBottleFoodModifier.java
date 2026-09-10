package justjabka.justraces.showcase.modifiers.food;

import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import io.papermc.paper.datacomponent.item.UseCooldown;
import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect;
import justjabka.justraces.api.interfaces.configurable.ItemModifierConfigurable;
import justjabka.justraces.api.modifiers.generic.BaseFoodModifier;
import justjabka.justraces.showcase.JustRacesShowcase;
import org.bukkit.NamespacedKey;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class HoneyBottleFoodModifier extends BaseFoodModifier implements ItemModifierConfigurable {

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "food/honey_bottle");
    }

    @Override
    public Consumer<Consumable.Builder> getConsumable() {
        final List<PotionEffect> consumeEffects = List.of(
                new PotionEffect(PotionEffectType.HASTE, getConfigInt("duration", "haste"), 0, false, true, true),
                new PotionEffect(PotionEffectType.SPEED, getConfigInt("duration", "speed"), 0, false, true, true),
                new PotionEffect(PotionEffectType.RESISTANCE, getConfigInt("duration", "resistance"), 0, false, true, true)
        );

        return builder -> builder
                .addEffect(ConsumeEffect.applyStatusEffects(
                    consumeEffects,
                    1f
                ))
                .hasConsumeParticles(false);
    }

    @Override
    public Consumer<FoodProperties.Builder> getFoodProperties() {
        return builder -> builder
                .nutrition(getConfigInt("nutrition"))
                .saturation(getConfigFloat("saturation"));
    }

    @Override
    public @Nullable UseCooldown getUseCooldown() {
        return null;
    }
}
