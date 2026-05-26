package justjabka.JustRaces.Modifiers.Food;

import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation;
import justjabka.JustRaces.Configs.Modifier.Food.PhantomMembraneFoodModifierConfig;
import justjabka.JustRaces.Managers.ModifierManager;
import justjabka.JustRaces.Modifiers.Generic.BaseFoodModifier;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("UnstableApiUsage")
public class PhantomMembraneFoodModifier extends BaseFoodModifier implements Listener {
    private final PhantomMembraneFoodModifierConfig config;

    public PhantomMembraneFoodModifier(NamespacedKey key, PhantomMembraneFoodModifierConfig config) {
        super(
                key,
                FoodProperties.food()
                        .canAlwaysEat(true)
                        .nutrition(config.nutritionAmount)
                        .saturation(config.saturationAmount)
                        .build(),

                Consumable.consumable()
                        .consumeSeconds(config.consumeSeconds)
                        .animation(ItemUseAnimation.EAT)
                        .hasConsumeParticles(true)
                        .build()
        );
        this.config = config;
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        String modifier = ModifierManager.getAppliedModifier(item);
        if (modifier == null) return;
        if (!modifier.equals(this.getKey().toString())) return;

        player.heal(config.healAmount, EntityRegainHealthEvent.RegainReason.EATING);
    }
}
