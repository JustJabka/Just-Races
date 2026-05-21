package justjabka.WeltenRaces.Listeners.Race;

import justjabka.WeltenRaces.Abilities.NoteAbility;
import justjabka.WeltenRaces.Configs.Race.FetrRaceConfig;
import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.Managers.RaceManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static justjabka.WeltenRaces.DataProvider.RaceProvider.FETR;
import static justjabka.WeltenRaces.Runnables.Race.FetrRaceRunnable.FETR_STATUS_KEY;

public class FetrRaceListener implements Listener {
    private final FetrRaceConfig config;
    private final Set<PotionEffect> hornBuffs;

    private static final Random RANDOM = new Random();

    private NoteAbility getNoteAbility() {
        return AbilityManager.getAbility(NoteAbility.class);
    }

    public FetrRaceListener(FetrRaceConfig config) {
        this.config = config;
        this.hornBuffs = Set.of(
                new PotionEffect(PotionEffectType.SPEED, config.hornBuffDuration, 1, false, true, true),
                new PotionEffect(PotionEffectType.ABSORPTION, config.hornBuffDuration, 5, false, true, true),
                new PotionEffect(PotionEffectType.RESISTANCE, config.hornBuffDuration, 1, false, true, true)
        );
    }

    public static boolean isIdol(Player player) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        return pdc.getOrDefault(FETR_STATUS_KEY, PersistentDataType.BOOLEAN, false);
    }

    @EventHandler
    public void onItemUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!RaceManager.isRace(player, FETR)) return;
        if (!AbilityManager.isActivationSlotSelected(player)) return;
        if (!isIdol(player)) return;

        ItemStack item = event.getItem();

        if (item == null) return;
        if (item.isEmpty()) return;

        if (item.getType() != Material.GOAT_HORN) return;

        if (!event.getAction().isRightClick()) return;
        if (event.getHand() != EquipmentSlot.HAND) return;

        if (player.getCooldown(item) > 0) return;

        Collection<Player> playersNearby = player.getLocation().getNearbyPlayers(config.hornBuffRadius);
        int playerCount = playersNearby.size();

        List<PotionEffect> buffs = hornBuffs.stream().toList();
        PotionEffect buff = buffs.get(RANDOM.nextInt(buffs.size()));

        playersNearby.forEach(p -> p.addPotionEffect(buff));

        NoteAbility noteAbility = getNoteAbility();
        if (noteAbility == null) return;

        noteAbility.addNotes(player, playerCount * config.hornBuffNotesPerPlayer);
    }
}
