package justjabka.WeltenRaces.Abilities;

import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.WeltenRaces;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import static justjabka.WeltenRaces.Listeners.FetrRaceListener.isIdol;

public class NoteAbility extends BaseAbility {
    public static final NamespacedKey NOTE_ABILITY_KEY = new NamespacedKey(WeltenRaces.NAMESPACE, "note");

    private static final int maxNoteAmount = 32;

    @Override
    public NamespacedKey getKey() {
        return NOTE_ABILITY_KEY;
    }

    @Override
    public Component getAbilityDisplay(Player player) {
        return Component
                .translatable("ability.note.amount")
                .fallback("%s/%s")
                .arguments(
                        Component.text(getNotes(player)),
                        Component.text(maxNoteAmount)
                )
                .color(NamedTextColor.LIGHT_PURPLE);
    }

    @Override
    public long getCooldownTicks() {
        return 20;
    }

    @EventHandler
    public void handleEntityDamageByEntity(EntityDamageByEntityEvent event) {
        super.handleEntityDamageByEntity(event);
    }

    @Override
    protected boolean onActivation(Player attacker, Object... ctx) {
        if (ctx.length == 0) return false;
        if (!(ctx[0] instanceof LivingEntity)) return false;

        addNotes(attacker, 1);
        return true;
    }

    @Override
    protected boolean canActivate(Player player) {
        return isIdol(player);
    }

    public void addNotes(Player player, int value) {
        int noteAmount = getNotes(player) + value;
        updateNotes(player, noteAmount);
    }

    public void removeNotes(Player player, int value) {
        int noteAmount = getNotes(player) - value;
        updateNotes(player, noteAmount);
    }

    public int getNotes(Player player) {
        return AbilityManager.getAbilityValue(player, getKey());
    }

    private void updateNotes(Player player, int value) {
        value = Math.clamp(value, 0, maxNoteAmount);

        AbilityManager.setAbilityValue(player, getKey(), value);
    }
}
