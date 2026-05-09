package justjabka.WeltenRaces.Modifiers.Contents.Food;

import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation;
import justjabka.WeltenRaces.Modifiers.Contents.Generic.BaseFoodModifier;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

@SuppressWarnings("UnstableApiUsage")
public class PhantomMembraneFoodModifier extends BaseFoodModifier implements Listener {
    public PhantomMembraneFoodModifier(String id) {
        super(
                id,
                FoodProperties.food()
                        .canAlwaysEat(true)
                        .nutrition(0)
                        .saturation(0)
                        .build(),

                Consumable.consumable()
                        .consumeSeconds(0.8f)
                        .animation(ItemUseAnimation.EAT)
                        .hasConsumeParticles(true)
                        .build()
        );
    }

    @EventHandler
    public void onItemConsume(PlayerItemConsumeEvent event) {
        event.getPlayer().sendMessage("test");
    }
}
