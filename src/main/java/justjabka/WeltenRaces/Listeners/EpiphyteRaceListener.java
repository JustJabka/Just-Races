package justjabka.WeltenRaces.Listeners;

import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.Types.Race;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EpiphyteRaceListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onPotionApply(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (RaceManager.getRace(player) != Race.EPIPHYTE) return;

        PotionEffect effect = event.getNewEffect();

        if (effect == null) return;
        if (effect.getType() != PotionEffectType.POISON) return;

        event.setCancelled(true); // HATE. LET ME TELL YOU HOVE MUCH I COME TO HATE PAPER API WHEN I STARTED WORKING ON THIS PROJECT. THERE ARE 8 BILLION NEURONS IN MY BRAIN AND IF ON EVERY NANOMETR OF EVERY CELL WILL BE CARWED WORD "HATE" THIS WOULD NOT TELL A SINGLE PERCENT OF MY HATE TO THIS API. HATE. HATE. HATE
        effect.withType(PotionEffectType.REGENERATION).apply(player);
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();

        if (RaceManager.getRace(player) != Race.EPIPHYTE) return;

        ItemStack consumedItem = event.getItem();
        if (consumedItem.getType() != Material.MOSS_BLOCK) return;

        player.heal(6, EntityRegainHealthEvent.RegainReason.EATING);
    }
}