package justjabka.WeltenRaces.Listeners;

import justjabka.WeltenRaces.Abilities.NoteAbility;
import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.Managers.RaceManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static justjabka.WeltenRaces.DataProvider.RaceProvider.FETR;

public class FetrRaceListener implements Listener {
    private static final Random RANDOM = new Random();

    private static final NoteAbility noteAbility = AbilityManager.getAbility(NoteAbility.class);

    private static final int notesPerPlayer = 2;

    private static final int hornBuffsDuration = 7 * 20;
    private static final double hornBuffsRadius = 15;
    private static final Set<PotionEffect> hornBuffs = Set.of(
            new PotionEffect(PotionEffectType.SPEED, hornBuffsDuration, 1, false, true, true),
            new PotionEffect(PotionEffectType.ABSORPTION, hornBuffsDuration, 5, false, true, true),
            new PotionEffect(PotionEffectType.RESISTANCE, hornBuffsDuration, 1, false, true, true)
    );

    @EventHandler
    public void onItemUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!RaceManager.isRace(player, FETR)) return;
        if (!AbilityManager.isActivationSlotSelected(player)) return;

        ItemStack item = event.getItem();

        if (item == null) return;
        if (item.isEmpty()) return;

        if (item.getType() != Material.GOAT_HORN) return;
        if (!event.getAction().isRightClick()) return;

        Collection<Player> playersNearby = player.getLocation().getNearbyPlayers(hornBuffsRadius);
        int playerCount = playersNearby.size();

        List<PotionEffect> buffs = hornBuffs.stream().toList();
        PotionEffect buff = buffs.get(RANDOM.nextInt(buffs.size()));

        playersNearby.forEach(p -> p.addPotionEffect(buff));

        if (noteAbility == null) return;
        noteAbility.addNotes(player, playerCount * notesPerPlayer);
    }
}
