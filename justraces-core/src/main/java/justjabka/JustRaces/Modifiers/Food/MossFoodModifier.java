package justjabka.JustRaces.Modifiers.Food;

import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import io.papermc.paper.datacomponent.item.UseCooldown;
import justjabka.JustRaces.Configs.Modifier.Food.MossFoodModifierConfig;
import justjabka.JustRaces.Managers.ModifierManager;
import justjabka.JustRaces.Modifiers.Generic.BaseFoodCooldownModifier;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("UnstableApiUsage")
public class MossFoodModifier extends BaseFoodCooldownModifier implements Listener {
    private final MossFoodModifierConfig config;

    public MossFoodModifier(NamespacedKey key, MossFoodModifierConfig config) {
        super(
                key,
                FoodProperties.food()
                        .nutrition(config.nutritionAmount)
                        .saturation(config.saturationAmount)
                        .canAlwaysEat(true)
                        .build(),

                Consumable.consumable().build(),
                UseCooldown.useCooldown(config.useCooldown).build()
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
