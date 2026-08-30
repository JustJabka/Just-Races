package justjabka.JustRacesShowcase.Modifiers.Food.Generic;

import justjabka.JustRaces.Managers.AbilityManager;
import justjabka.JustRaces.Modifiers.Generic.BaseFoodModifier;
import justjabka.JustRacesShowcase.Abilities.FrogTongueAbility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public abstract class BaseFrogTongueTypeChangerFoodModifier extends BaseFoodModifier implements Listener {
    public abstract FrogTongueAbility.TongueType getFrogTongueType();

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (!isRequiredModifier(item)) return;

        FrogTongueAbility frogTongueAbility = AbilityManager.getAbility(FrogTongueAbility.class);
        if (frogTongueAbility == null) return;

        if (!frogTongueAbility.playerHasAbility(player)) return;
        frogTongueAbility.setTongueType(player, getFrogTongueType());
    }
}
