package justjabka.JustRaces.Abilities;

import justjabka.JustRaces.Abilities.Generic.BaseValidationAbility;
import justjabka.JustRaces.JustRaces;
import justjabka.JustRaces.Managers.AbilityManager;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import static justjabka.JustRaces.Listeners.Race.FetrRaceListener.isIdol;

public class StageAbility extends BaseValidationAbility {
    public static final NamespacedKey STAGE_ABILITY_KEY = new NamespacedKey(JustRaces.NAMESPACE, "stage");

    private static final int requiredNotes = 24;
    private static final int duration = 15 * 20;

    private NoteAbility getNoteAbility() {
        return AbilityManager.getAbility(NoteAbility.class);
    }

    @Override
    public NamespacedKey getKey() {
        return STAGE_ABILITY_KEY;
    }

    @Override
    public long getCooldownTicks() {
        return 0;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        clearStage(player);
    }

    @EventHandler
    public void handleInteract(PlayerInteractEvent event) {
        super.handleInteract(event);
    }

    @Override
    protected boolean onActivation(Player player, Object... ctx) {
        NoteAbility noteAbility = getNoteAbility();
        if (noteAbility == null) return false;

        giveStage(player);
        noteAbility.removeNotes(player, requiredNotes);

        return true;
    }

    @Override
    public void onDeactivation(Player player) {
        clearStage(player);
    }

    public void giveStage(Player player) {
        AbilityManager.setAbilityState(player, getKey(), true);

        Bukkit.getScheduler().runTaskLater(JustRaces.INSTANCE, () -> clearStage(player), duration);
    }

    public void clearStage(Player player) {
        AbilityManager.setAbilityState(player, getKey(), false);
    }

    @Override
    protected boolean canActivate(Player player) {
        NoteAbility noteAbility = getNoteAbility();
        if (noteAbility == null) return false;

        if (!isIdol(player)) return false;
        return noteAbility.getNotes(player) >= requiredNotes;
    }
}
