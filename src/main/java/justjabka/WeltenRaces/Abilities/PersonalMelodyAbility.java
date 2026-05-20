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

import java.util.Set;

import static justjabka.WeltenRaces.Listeners.FetrRaceListener.isIdol;

public class PersonalMelodyAbility extends BaseAbility {
    private static final int duration = 30 * 20;
    private static final int requiredNotes = 24;

    private static final Set<PotionEffect> humanBuffEffects = Set.of(
            new PotionEffect(PotionEffectType.SATURATION, duration, 0, false, false, true)
    );

    private static final Set<PotionEffect> armatBuffEffects = Set.of(
            new PotionEffect(PotionEffectType.HEALTH_BOOST, duration, 4, false, false, true),
            new PotionEffect(PotionEffectType.REGENERATION, duration, 1, false, false, true)

    );

    private static final PotionEffect skyzernBuffEffect =
            new PotionEffect(PotionEffectType.STRENGTH, duration, 0, false, false, true);
    private static final Range<Integer> skyzernBuffWeak = Range.between(50, 99);
    private static final Range<Integer> skyzernBuffNormal = Range.between(100, 219);
    private static final int skyzernBuffStrong = 220;


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

        noteAbility.removeNotes(player, requiredNotes);
        givePersonalMelodyBuff(target);

        return true;
    }

    private void givePersonalMelodyBuff(Player target) {
        NamespacedKey race = RaceManager.getRace(target).getKey();

        if (race.equals(RaceProvider.HUMAN)) giveHumanBuff(target);
        else if (race.equals(RaceProvider.ARMAT)) giveArmatBuff(target);
        else if (race.equals(RaceProvider.SKYZERN)) giveSkyzernBuff(target);
    }

    private void giveHumanBuff(Player player) {
        humanBuffEffects.forEach(player::addPotionEffect);
    }

    private void giveArmatBuff(Player player) {
        armatBuffEffects.forEach(player::addPotionEffect);
    }

    private void giveSkyzernBuff(Player player) {
        int y = player.getLocation().getBlock().getY();
        int amplifier;

        if (y >= skyzernBuffStrong) amplifier = 4;
        else if (skyzernBuffNormal.contains(y)) amplifier = 2;
        else if (skyzernBuffWeak.contains(y)) amplifier = 0;
        else return;

        player.addPotionEffect(skyzernBuffEffect.withAmplifier(amplifier));
    }

    @Override
    protected boolean canActivate(Player player) {
        NoteAbility noteAbility = getNoteAbility();
        if (noteAbility == null) return false;

        if (!isIdol(player)) return false;
        return noteAbility.getNotes(player) >= requiredNotes;
    }
}
