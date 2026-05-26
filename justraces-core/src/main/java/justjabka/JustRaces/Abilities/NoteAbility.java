package justjabka.JustRaces.Abilities;

import justjabka.JustRaces.Abilities.Generic.BaseAbility;
import justjabka.JustRaces.JustRacesAPI;
import justjabka.JustRaces.Managers.AbilityManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static justjabka.JustRaces.Abilities.StageAbility.STAGE_ABILITY_KEY;
import static justjabka.JustRaces.Listeners.Race.FetrRaceListener.isIdol;

public class NoteAbility extends BaseAbility {
    public static final NamespacedKey NOTE_ABILITY_KEY = new NamespacedKey(JustRacesAPI.NAMESPACE, "note");

    private static final int maxNoteAmount = 32;
    private static final int notePerAttack = 1;

    private static final double stageHealAmount = 1;
    private static final int stageBonusNotes = 1;

    private static final PotionEffect stageAlleyEffect = new PotionEffect(PotionEffectType.SPEED, 5 * 20, 1, false, true, true);
    private static final PotionEffect stageEnemyEffect = new PotionEffect(PotionEffectType.SLOWNESS, 3 * 20, 1, false, true, true);

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
        if (!(ctx[0] instanceof LivingEntity victim)) return false;

        addNotes(attacker, notePerAttack);
        handleStageAttack(attacker, victim);
        return true;
    }

    @Override
    protected boolean canActivate(Player player) {
        return isIdol(player);
    }

    private void handleStageAttack(Player attacker, LivingEntity victim) {
        if (!AbilityManager.isAbilityActive(attacker, STAGE_ABILITY_KEY)) return;

        if (victim instanceof Player target) {
            handleStageAlley(attacker, target);
        } else {
            handleStageEnemy(attacker, victim);
        }

        addNotes(attacker, stageBonusNotes);
    }

    private void handleStageAlley(Player player, Player alley) {
        alley.heal(stageHealAmount);

        if (!hasGoatHorn(player)) return;
        alley.addPotionEffect(stageAlleyEffect);
    }

    private void handleStageEnemy(Player player, LivingEntity enemy) {
        player.heal(stageHealAmount);

        if (!hasGoatHorn(player)) return;
        enemy.addPotionEffect(stageEnemyEffect);
    }

    private static boolean hasGoatHorn(Player player) {
        ItemStack item = player.getInventory().getItem(AbilityManager.getActivationSlot());

        if (item == null) return false;
        if (item.isEmpty()) return false;

        return item.getType() == Material.GOAT_HORN;
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
