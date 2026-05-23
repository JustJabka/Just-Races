package justjabka.WeltenRaces.Listeners.Race;

import justjabka.WeltenRaces.DataProvider.ItemTypeTagKeysProvider;
import justjabka.WeltenRaces.DataProvider.RaceProvider;
import justjabka.WeltenRaces.Listeners.Generic.BaseRaceListener;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;

public class PhantomRaceListener extends BaseRaceListener {
    private static final Collection<ItemType> isMeat = ItemTypeTagKeysProvider.getTagValues(ItemTypeTagKeysProvider.IS_MEAT);
    private static final PotionEffect meatBonusEffect = new PotionEffect(
            PotionEffectType.REGENERATION,
            0,
            0,
            false,
            false,
            false
    );

    @Override
    public NamespacedKey getRaceKey() {
        return RaceProvider.PHANTOM;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player victim)) return;
        if (!(event.getDamager() instanceof Player attacker)) return;

        if (!isRequiredRace(attacker)) return;

        if (!hasInsomnia(victim)) return;

        double insomniaDamageBonus = getConfig().node("insomnia_damage_bonus").getDouble();
        event.setDamage(event.getDamage() + insomniaDamageBonus);
    }

    private static boolean hasInsomnia(Player victim) {
        int ticksSinceRest = victim.getStatistic(Statistic.TIME_SINCE_REST);
        int daysSinceRest = ticksSinceRest / 24000;

        return daysSinceRest >= 3;
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();

        if (!isRequiredRace(player)) return;

        Material consumedMaterial = event.getItem().getType();
        ItemType consumedType = consumedMaterial.asItemType();
        
        if (!isMeat.contains(consumedType)) return;
        giveMeatBonus(player);
    }

    private void giveMeatBonus(Player player) {
        int meatBonusAmount = getConfig().node("meat_bonus", "food_amount").getInt();
        int meatBonusRegenerationDuration = getConfig().node("meat_bonus", "regeneration_duration").getInt() * 20;
        WeltenRaces.LOGGER.info(String.valueOf(meatBonusAmount));

        player.setFoodLevel(player.getFoodLevel() + meatBonusAmount);
        player.addPotionEffect(meatBonusEffect.withDuration(meatBonusRegenerationDuration));
    }
}
