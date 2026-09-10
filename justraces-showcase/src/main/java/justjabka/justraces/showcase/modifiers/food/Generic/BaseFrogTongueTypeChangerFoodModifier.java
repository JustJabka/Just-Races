package justjabka.justraces.showcase.modifiers.food.Generic;

import justjabka.justraces.api.managers.AbilityManager;
import justjabka.justraces.api.modifiers.generic.BaseFoodModifier;
import justjabka.justraces.showcase.abilities.FrogTongueAbility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public abstract class BaseFrogTongueTypeChangerFoodModifier extends BaseFoodModifier implements Listener {
    public abstract FrogTongueAbility.TongueType getFrogTongueType();

    @EventHandler(ignoreCancelled = true)
    public void onConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (!isRequiredModifier(item)) return;

        FrogTongueAbility frogTongueAbility = AbilityManager.getByClass(FrogTongueAbility.class);
        if (frogTongueAbility == null) return;

        if (!frogTongueAbility.playerHasAbility(player)) return;
        frogTongueAbility.setTongueType(player, getFrogTongueType());
    }
}
