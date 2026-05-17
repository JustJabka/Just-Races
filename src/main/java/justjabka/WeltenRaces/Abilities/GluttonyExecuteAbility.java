package justjabka.WeltenRaces.Abilities;

import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.Configs.Ability.GluttonyExecuteAbilityConfig;
import justjabka.WeltenRaces.DataProvider.EntityTypeTagKeysProvider;
import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.WeltenRaces;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jspecify.annotations.Nullable;

import java.util.Collection;

import static justjabka.WeltenRaces.Abilities.TrueFormAbility.TRUE_FORM_KEY;

public class GluttonyExecuteAbility extends BaseAbility {
    private final GluttonyExecuteAbilityConfig config;

    private static final Sound consumeSound = Sound.ENTITY_EVOKER_FANGS_ATTACK;
    private static final PotionEffect consumeEffect = new PotionEffect(PotionEffectType.SATURATION, 10, 0, false, false, false);

    private static final Collection<EntityType> gluttonyExecuteIgnored = EntityTypeTagKeysProvider.getTagValues(EntityTypeTagKeysProvider.GLUTTONY_EXECUTE_IGNORED);

    public GluttonyExecuteAbility(GluttonyExecuteAbilityConfig config) {
        this.config = config;
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(WeltenRaces.NAMESPACE, "gluttony_execute");
    }

    @Override
    public long getCooldownTicks() {
        return 0;
    }

    @Override
    public Component getAbilityDisplay(Player player) {
        if (!AbilityManager.isAbilityActive(player, TRUE_FORM_KEY)) return Component.empty();
        return super.getAbilityDisplay(player);
    }

    @EventHandler
    public void handleEntityInteract(PlayerInteractEntityEvent event) {
        super.handleEntityInteract(event);
    }

    @Override
    protected boolean onActivation(Player player, Object... ctx) {
        LivingEntity target = getValidTarget(ctx);
        if (target == null) return false;

        AttributeInstance maxHealthInstance = target.getAttribute(Attribute.MAX_HEALTH);
        if (maxHealthInstance == null) return false;

        double maxHealth = maxHealthInstance.getValue();
        double currentHealth = target.getHealth();

        int secondsToExtend = calcSecondsToExtend(maxHealth, currentHealth);
        if (secondsToExtend == 0) return false;

        consumeTarget(player, target, secondsToExtend);
        return true;
    }

    private static @Nullable LivingEntity getValidTarget(Object[] ctx) {
        if (ctx.length == 0 || !(ctx[0] instanceof LivingEntity target)) return null;

        if (target.isDead()) return null;

        if (gluttonyExecuteIgnored.contains(target.getType())) return null;

        return target;
    }

    private int calcSecondsToExtend(double maxHealth, double currentHealth) {
        int secondsToExtend = 0;

        if (maxHealth <= config.smallKillMaxHealth) {
            secondsToExtend = config.smallKillSecondsToExtend;
        }

        if (maxHealth <= config.bigKillMaxHealth && currentHealth < config.bigKillRequiredHealth) {
            secondsToExtend = config.bigKillSecondsToExtend;
        }

        return secondsToExtend;
    }

    private static void consumeTarget(Player player, LivingEntity target, int secondsToExtend) {
        target.setHealth(0);

        player.addPotionEffect(consumeEffect);
        TrueFormAbility.extendTrueForm(player, secondsToExtend);
        player.getWorld().playSound(player.getLocation(), consumeSound, SoundCategory.PLAYERS, 1, 1);
    }

    @Override
    protected boolean canActivate(Player player) {
        if (!AbilityManager.isAbilityActive(player, TRUE_FORM_KEY)) return false;
        return player.isSneaking();
    }
}
