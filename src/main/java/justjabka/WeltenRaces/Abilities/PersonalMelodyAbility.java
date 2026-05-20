package justjabka.WeltenRaces.Abilities;

import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.DataProvider.RaceProvider;
import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.Managers.AttributeManager;
import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.WeltenRaces;
import org.apache.commons.lang3.Range;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.Set;

import static justjabka.WeltenRaces.Listeners.FetrRaceListener.isIdol;

public class PersonalMelodyAbility extends BaseAbility {
    public static final NamespacedKey PERSONAL_MELODY_ABILITY_KEY = new NamespacedKey(WeltenRaces.NAMESPACE, "personal_melody");

    private static final int duration = 30 * 20;
    private static final int requiredNotes = 24;

    // Human
    private static final Set<PotionEffect> humanBuffEffects = Set.of(
            new PotionEffect(PotionEffectType.SATURATION, duration, 0, false, false, true)
    );

    // Armat
    private static final Set<PotionEffect> armatBuffEffects = Set.of(
            new PotionEffect(PotionEffectType.HEALTH_BOOST, duration, 4, false, false, true),
            new PotionEffect(PotionEffectType.REGENERATION, duration, 1, false, false, true)

    );

    // Skyzern
    private static final PotionEffect skyzernBuffEffect =
            new PotionEffect(PotionEffectType.STRENGTH, duration, 0, false, false, true);

    private static final Range<Integer> skyzernBuffWeak = Range.between(50, 99);
    private static final Range<Integer> skyzernBuffNormal = Range.between(100, 219);
    private static final int skyzernBuffStrong = 220;

    // Epiphyte
    private static final Set<PotionEffect> epiphyteBuffEffects = Set.of(
            new PotionEffect(PotionEffectType.STRENGTH, duration, 1, false, false, true)
    );
    private static final PotionEffect epiphyteVictimEffect =
            new PotionEffect(PotionEffectType.WEAKNESS, 10 * 20, 0, false, true, true);

    // Phantom
    private static final double phantomBuffAdditionalDamage = 6;
    private static final Set<PotionEffect> phantomBuffEffects = Set.of(
            new PotionEffect(PotionEffectType.FIRE_RESISTANCE, duration, 0, false, false, true),
            new PotionEffect(PotionEffectType.INVISIBILITY, duration, 0, false, false, true),
            new PotionEffect(PotionEffectType.SPEED, duration, 2, false, false, true)
    );

    // Lizard
    private final Map<Attribute, AttributeModifier> lizardBuffModifier = Map.of(
            Attribute.ATTACK_DAMAGE, new AttributeModifier(
                    getKey(),
                    2,
                    AttributeModifier.Operation.ADD_NUMBER
            )
    );

    private NoteAbility getNoteAbility() {
        return AbilityManager.getAbility(NoteAbility.class);
    }

    @Override
    public NamespacedKey getKey() {
        return PERSONAL_MELODY_ABILITY_KEY;
    }

    @Override
    public long getCooldownTicks() {
        return 0;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!AbilityManager.isAbilityActive(player, getKey())) return;
        unmarkBuffed(player);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player attacker)) return;
        if (!(event.getEntity() instanceof LivingEntity victim)) return;

        if (!AbilityManager.isAbilityActive(attacker, getKey())) return;

        if (RaceManager.isRace(attacker, RaceProvider.EPIPHYTE)) {
            victim.addPotionEffect(epiphyteVictimEffect);
        }
        else if (RaceManager.isRace(attacker, RaceProvider.PHANTOM)) {
            event.setDamage(event.getDamage() + phantomBuffAdditionalDamage);

            attacker.removePotionEffect(PotionEffectType.INVISIBILITY);
            unmarkBuffed(attacker);
        }
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
        else if (race.equals(RaceProvider.EPIPHYTE)) giveEpiphyteBuff(target);
        else if (race.equals(RaceProvider.PHANTOM)) givePhantomBuff(target);
        else if (race.equals(RaceProvider.LIZARD)) giveLizardBuff(target);
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

    private void giveEpiphyteBuff(Player player) {
        epiphyteBuffEffects.forEach(player::addPotionEffect);
        markBuffed(player);
    }

    private void givePhantomBuff(Player player) {
        phantomBuffEffects.forEach(player::addPotionEffect);
        markBuffed(player);
    }

    private void giveLizardBuff(Player player) {
        AttributeManager.addModifiers(player, lizardBuffModifier);
        markBuffed(player);

        TrueFormAbility trueFormAbility = AbilityManager.getAbility(TrueFormAbility.class);
        if (trueFormAbility == null) return;

        if (AbilityManager.isAbilityActive(player, trueFormAbility.getKey())) return;
        trueFormAbility.giveTrueForm(player);
    }

    public void markBuffed(Player player) {
        AbilityManager.setAbilityState(player, getKey(), true);
        Bukkit.getScheduler().runTaskLater(WeltenRaces.INSTANCE, () -> unmarkBuffed(player), duration);
    }

    public void unmarkBuffed(Player player) {
        AbilityManager.setAbilityState(player, getKey(), false);

        if (!AttributeManager.hasModifiers(player, lizardBuffModifier)) return;
        AttributeManager.removeModifiers(player, lizardBuffModifier);
    }

    @Override
    protected boolean canActivate(Player player) {
        NoteAbility noteAbility = getNoteAbility();
        if (noteAbility == null) return false;

        if (!isIdol(player)) return false;
        return noteAbility.getNotes(player) >= requiredNotes;
    }
}
