package justjabka.WeltenRaces.Listeners;

import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.Abilities.NoteAbility;
import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.Registries.AbilitiesRegistry;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

import static justjabka.WeltenRaces.Abilities.NoteAbility.NOTE_ABILITY_KEY;
import static justjabka.WeltenRaces.DataProvider.RaceProvider.FETR;

public class FetrRaceListener implements Listener {
    private static final Random RANDOM = new Random();
    private static final Map<NamespacedKey, BaseAbility> abilityRegistry = AbilitiesRegistry.getAbilities();

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
        if (!AbilityManager.hasActivationSlotSelected(player)) return;

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

        if (abilityRegistry.get(NOTE_ABILITY_KEY) instanceof NoteAbility ability) {
            ability.addNotes(player, playerCount * notesPerPlayer);
        }
    }
}
