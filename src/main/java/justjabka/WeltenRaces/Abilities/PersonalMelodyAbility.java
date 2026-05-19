package justjabka.WeltenRaces.Abilities;

import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.DataProvider.RaceProvider;
import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.WeltenRaces;
import org.apache.commons.lang3.Range;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static justjabka.WeltenRaces.Listeners.FetrRaceListener.isIdol;

public class PersonalMelodyAbility extends BaseAbility {
    private static final int duration = 30 * 20;
    private static final int requiredNotes = 24;

    private NoteAbility getNoteAbility() {
        return AbilityManager.getAbility(NoteAbility.class);
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(WeltenRaces.NAMESPACE, "personal_melody");
    }

    @Override
    public long getCooldownTicks() {
        return 0;
    }

    @EventHandler
    public void handleEntityInteract(PlayerInteractEntityEvent event) {
        super.handleEntityInteract(event);
    }

    @Override
    protected boolean onActivation(Player player, Object... ctx) {
        NoteAbility noteAbility = getNoteAbility();
        if (noteAbility == null) return false;

        if (ctx.length == 0) return false;
        if (!(ctx[0] instanceof Player target)) return false;

        noteAbility.removeNotes(player, 24);
        givePersonalMelodyBuff(player, target);

        return true;
    }

    private void givePersonalMelodyBuff(Player player, Player target) {
        NamespacedKey race = RaceManager.getRace(target).getKey();

        if (race.equals(RaceProvider.HUMAN)) giveHumanBuff(target);
        else if (race.equals(RaceProvider.ARMAT)) giveArmatBuff(target);
        else if (race.equals(RaceProvider.SKYZERN)) giveSkyzernBuff(target);
    }

    private void giveHumanBuff(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, duration, 0, false, false, true));
    }

    private void giveArmatBuff(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, duration, 4, false, false, true));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, duration, 1, false, false, true));
    }

    private void giveSkyzernBuff(Player player) {
        int y = player.getLocation().getBlock().getY();

        Range<Integer> weakBuff = Range.between(50, 99);
        Range<Integer> normalBuff = Range.between(100, 219);
        int strongBuff = 220;

        if (y >= strongBuff) player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, duration, 4, false, false, true));
        if (normalBuff.contains(y)) player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, duration, 2, false, false, true));
        if (weakBuff.contains(y)) player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, duration, 0, false, false, true));
    }

    @Override
    protected boolean canActivate(Player player) {
        NoteAbility noteAbility = getNoteAbility();
        if (noteAbility == null) return false;

        if (!isIdol(player)) return false;
        return noteAbility.getNotes(player) >= requiredNotes;
    }
}
