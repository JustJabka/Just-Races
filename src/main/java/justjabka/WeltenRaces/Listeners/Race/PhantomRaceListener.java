package justjabka.WeltenRaces.Listeners.Race;

import justjabka.WeltenRaces.Configs.Race.PhantomRaceConfig;
import justjabka.WeltenRaces.DataProvider.ItemTypeTagKeysProvider;
import justjabka.WeltenRaces.DataProvider.RaceProvider;
import justjabka.WeltenRaces.Managers.RaceManager;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PhantomRaceListener implements Listener {
    private final PhantomRaceConfig config;

    public PhantomRaceListener(PhantomRaceConfig config) {
        this.config = config;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player victim)) return;
        if (!(event.getDamager() instanceof Player attacker)) return;

        if (!RaceManager.isRace(attacker, RaceProvider.PHANTOM)) return;

        int ticksSinceRest = victim.getStatistic(Statistic.TIME_SINCE_REST);
        int daysSinceRest = ticksSinceRest / 24000;

        boolean hasInsomnia = daysSinceRest >= 3;
        if (!hasInsomnia) return;

        event.setDamage(event.getDamage() + config.insomniaDamageBonus);
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();

        if (!RaceManager.isRace(player, RaceProvider.PHANTOM)) return;

        Material consumedMaterial = event.getItem().getType();
        ItemType consumedType = consumedMaterial.asItemType();

        if (ItemTypeTagKeysProvider.getTagValues(ItemTypeTagKeysProvider.IS_MEAT).contains(consumedType)) {
            player.setFoodLevel(player.getFoodLevel() + config.meatBonusFoodAmount);

            if (config.meatBonusRegenerationDuration <= 0) return;
            player.addPotionEffect(new PotionEffect(
                    PotionEffectType.REGENERATION,
                    config.meatBonusRegenerationDuration,
                    0,
                    false,
                    false,
                    false
            ));
        }
    }
}
