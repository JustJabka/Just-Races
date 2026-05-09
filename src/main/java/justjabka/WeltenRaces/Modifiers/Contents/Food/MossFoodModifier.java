package justjabka.WeltenRaces.Modifiers.Contents.Food;

import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import io.papermc.paper.datacomponent.item.UseCooldown;
import justjabka.WeltenRaces.Managers.ModifierManager;
import justjabka.WeltenRaces.Modifiers.Contents.Generic.BaseFoodCooldownModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("UnstableApiUsage")
public class MossFoodModifier extends BaseFoodCooldownModifier implements Listener {
    public MossFoodModifier(String id) {
        super(
                id,
                FoodProperties.food()
                        .nutrition(0)
                        .saturation(0)
                        .canAlwaysEat(true)
                        .build(),

                Consumable.consumable().build(),
                UseCooldown.useCooldown(60).build()
        );
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        String modifier = ModifierManager.getAppliedModifier(item);
        if (!modifier.equals(this.getId())) return;

        player.heal(6, EntityRegainHealthEvent.RegainReason.EATING);
    }
}
