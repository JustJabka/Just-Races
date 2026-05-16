package justjabka.WeltenRaces.Abilities;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.persistence.PersistentDataContainerView;
import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.Configs.Ability.PoisonousSplitAbilityConfig;
import justjabka.WeltenRaces.Types.AbilityActivateAction;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

public class PoisonousSplitAbility extends BaseAbility {
    private final PoisonousSplitAbilityConfig config;
    private final PotionEffect projectileHitEffect;
    private static final ItemStack projectileDisplayItem = getPoisonousSplitDisplayItem();

    public PoisonousSplitAbility(PoisonousSplitAbilityConfig config) {
        this.config = config;
        this.projectileHitEffect = new PotionEffect(
                PotionEffectType.POISON,
                config.effectDuration,
                config.effectAmplifier,
                false,
                true,
                true
        );
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(WeltenRaces.NAMESPACE, "poisonous_split");
    }

    @Override
    public long getCooldownTicks() {
        return config.cooldown;
    }

    @EventHandler
    public void handleInteract(PlayerInteractEvent event) {
        super.handleInteract(event);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Snowball projectile)) return;
        if (!(event.getHitEntity() instanceof LivingEntity victim)) return;

        PersistentDataContainerView pdc = projectile.getPersistentDataContainer();
        boolean isPoisonousSplit = pdc.has(getKey());

        if (!isPoisonousSplit) return;
        onPoisonousSplitProjectileHit(projectile, victim);
    }

    private void onPoisonousSplitProjectileHit(Snowball projectile, LivingEntity victim) {
        ProjectileSource projectileSource = projectile.getShooter();
        Entity causingEntity = projectileSource instanceof Entity shooter ? shooter : projectile;

        DamageSource damageSource = DamageSource.builder(DamageType.GENERIC)
                .withDirectEntity(projectile)
                .withCausingEntity(causingEntity)
                .build();

        victim.damage(config.projectileDamage, damageSource);
        victim.addPotionEffect(projectileHitEffect);
    }

    @Override
    protected boolean onActivation(Player player, Object... ctx) {
        Snowball projectile = player.launchProjectile(Snowball.class, null);
        projectile.getPersistentDataContainer().set(getKey(), PersistentDataType.BOOLEAN, true);
        projectile.setItem(projectileDisplayItem);

        return true;
    }

    @SuppressWarnings("UnstableApiUsage")
    private static ItemStack getPoisonousSplitDisplayItem() {
        ItemStack item = new ItemStack(Material.POISONOUS_POTATO);
        item.setData(DataComponentTypes.ITEM_MODEL, new NamespacedKey(WeltenRaces.NAMESPACE, "poisonous_split"));

        return item;
    }


    @Override
    protected boolean interactionAction(PlayerInteractEvent event, Player player) {
        return AbilityActivateAction.LEFT_CLICK.check(event, player);
    }
}
