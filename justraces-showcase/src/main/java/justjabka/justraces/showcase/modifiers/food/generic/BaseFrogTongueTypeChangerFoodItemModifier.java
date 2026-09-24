package justjabka.justraces.showcase.modifiers.food.generic;

import justjabka.justraces.api.managers.AbilityManager;
import justjabka.justraces.api.itemmodifiers.generic.BaseFoodItemModifier;
import justjabka.justraces.showcase.abilities.FrogTongueAbility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public abstract class BaseFrogTongueTypeChangerFoodItemModifier extends BaseFoodItemModifier implements Listener {
    public abstract FrogTongueAbility.TongueType getFrogTongueType();

    @EventHandler(ignoreCancelled = true)
    public void onConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (!isRequiredModifier(item)) return;

        FrogTongueAbility frogTongueAbility = AbilityManager.getByClass(FrogTongueAbility.class);
        if (frogTongueAbility == null) return;

        if (!frogTongueAbility.isRequiredAbility(player)) return;
        frogTongueAbility.setTongueType(player, getFrogTongueType());
    }
}
