package justjabka.JustRacesShowcase.Abilities;

import justjabka.JustRaces.Abilities.Generic.BaseValidationAbility;
import justjabka.JustRacesShowcase.JustRacesShowcase;
import justjabka.JustRaces.Managers.AbilityManager;
import justjabka.JustRaces.Managers.AttributeManager;
import org.apache.commons.lang3.Range;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.Set;

import static justjabka.JustRacesShowcase.Listeners.Race.FetrRaceListener.isIdol;

public class NoteBuffAbility extends BaseValidationAbility {
    private static final int requiredNotes = 12;
    private static final int duration = 10 * 20;

    private NoteAbility getNoteAbility() {
        return AbilityManager.getAbility(NoteAbility.class);
    }

    private final Set<PotionEffect> targetEffects = Set.of(
            new PotionEffect(PotionEffectType.SPEED, duration, 0, false, true, true),
            new PotionEffect(PotionEffectType.ABSORPTION, duration, 3, false, true, true)
    );

    private final Map<Attribute, AttributeModifier> targetModifiers = Map.of(
            Attribute.ATTACK_SPEED, new AttributeModifier(
                    getKey(),
                    0.6,
                    AttributeModifier.Operation.ADD_NUMBER
            )
    );

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "note_buff");
    }

    @Override
    public long getCooldownTicks() {
        return 0;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        clearNoteBuff(player);
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

        if (AttributeManager.hasModifiers(target, targetModifiers)) return false;

        giveNoteBuff(target);
        noteAbility.removeNotes(player, requiredNotes);

        return true;
    }

    @Override
    public void onDeactivation(Player player) {
        clearNoteBuff(player);
    }

    public void giveNoteBuff(Player player) {
        targetEffects.forEach(player::addPotionEffect);
        AttributeManager.addModifiers(player, targetModifiers);

        Bukkit.getScheduler().runTaskLater(JustRacesShowcase.INSTANCE, () -> clearNoteBuff(player), duration);
    }

    public void clearNoteBuff(Player player) {
        targetEffects.forEach(effect -> player.removePotionEffect(effect.getType()));
        AttributeManager.removeModifiers(player, targetModifiers);
    }

    @Override
    protected boolean canActivate(Player player) {
        NoteAbility noteAbility = getNoteAbility();
        if (noteAbility == null) return false;

        if (!isIdol(player)) return false;
        if (player.isSneaking()) return false;

        int notes = noteAbility.getNotes(player);
        Range<Integer> noteRange = Range.between(requiredNotes, 23);

        return noteRange.contains(notes);
    }
}
