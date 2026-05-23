package justjabka.WeltenRaces.Listeners.Race;

import justjabka.WeltenRaces.Abilities.NoteAbility;
import justjabka.WeltenRaces.DataProvider.RaceProvider;
import justjabka.WeltenRaces.Listeners.Generic.BaseRaceListener;
import justjabka.WeltenRaces.Managers.AbilityManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
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

import static justjabka.WeltenRaces.Runnables.Race.FetrRaceRunnable.FETR_STATUS_KEY;

public class FetrRaceListener extends BaseRaceListener {
    private final Set<PotionEffect> hornBuffs = Set.of(
            new PotionEffect(PotionEffectType.SPEED, 0, 1, false, true, true),
            new PotionEffect(PotionEffectType.ABSORPTION, 0, 5, false, true, true),
            new PotionEffect(PotionEffectType.RESISTANCE, 0, 1, false, true, true)
    );

    private static final Random RANDOM = new Random();

    private NoteAbility getNoteAbility() {
        return AbilityManager.getAbility(NoteAbility.class);
    }

    public static boolean isIdol(Player player) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        return pdc.getOrDefault(FETR_STATUS_KEY, PersistentDataType.BOOLEAN, false);
    }

    @Override
    public NamespacedKey getRaceKey() {
        return RaceProvider.FETR;
    }

    @EventHandler
    public void onItemUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!isRequiredRace(player)) return;
        if (!AbilityManager.isActivationSlotSelected(player)) return;
        if (!isIdol(player)) return;

        ItemStack item = event.getItem();

        if (item == null) return;
        if (item.isEmpty()) return;

        if (item.getType() != Material.GOAT_HORN) return;

        if (!event.getAction().isRightClick()) return;
        if (event.getHand() != EquipmentSlot.HAND) return;

        if (player.getCooldown(item) > 0) return;

        int hornBuffDuration = getConfig().node("horn_buff", "duration").getInt() * 20;
        double hornBuffRadius = getConfig().node("horn_buff", "radius").getDouble();
        int hornBuffNotesPerPlayer = getConfig().node("horn_buff", "notes_per_player").getInt();

        Collection<Player> playersNearby = player.getLocation().getNearbyPlayers(hornBuffRadius);
        int playerCount = playersNearby.size();

        List<PotionEffect> buffs = hornBuffs.stream().toList();
        PotionEffect buff = buffs.get(RANDOM.nextInt(buffs.size()));

        playersNearby.forEach(p -> p.addPotionEffect(buff.withDuration(hornBuffDuration)));

        NoteAbility noteAbility = getNoteAbility();
        if (noteAbility == null) return;

        noteAbility.addNotes(player, playerCount * hornBuffNotesPerPlayer);
    }
}
