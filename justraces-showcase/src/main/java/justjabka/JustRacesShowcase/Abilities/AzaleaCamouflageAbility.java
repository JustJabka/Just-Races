package justjabka.JustRacesShowcase.Abilities;

import io.papermc.paper.persistence.PersistentDataContainerView;
import io.papermc.paper.registry.keys.tags.DamageTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import justjabka.JustRaces.Abilities.Generic.BaseAbility;
import justjabka.JustRacesShowcase.Configs.Ability.AzaleaCamouflageAbilityConfig;
import justjabka.JustRacesShowcase.DataProvider.DamageTypeTagKeysProvider;
import justjabka.JustRacesShowcase.JustRacesShowcase;
import justjabka.JustRaces.Managers.AttributeManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Statistic;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AzaleaCamouflageAbility extends BaseAbility {
    private final AzaleaCamouflageAbilityConfig config;
    private static final TagKey<DamageType> BYPASSES_EFFECTS = DamageTypeTagKeys.BYPASSES_EFFECTS;
    private static final Collection<DamageType> BYPASSES_EFFECTS_VALUES = DamageTypeTagKeysProvider.getTagValues(BYPASSES_EFFECTS);

    private final Set<PotionEffect> userEffects;
    private final AttributeModifier minValueAttribute = new AttributeModifier(
            getKey(),
            Integer.MIN_VALUE,
            AttributeModifier.Operation.ADD_NUMBER
    );
    private final Map<Attribute, AttributeModifier> camoAttributes = Map.of(
            Attribute.MOVEMENT_SPEED, minValueAttribute,
            Attribute.JUMP_STRENGTH, minValueAttribute
    );

    public AzaleaCamouflageAbility(AzaleaCamouflageAbilityConfig config) {
        this.config = config;
        this.userEffects = Set.of(
                new PotionEffect(
                        PotionEffectType.INVISIBILITY,
                        PotionEffect.INFINITE_DURATION,
                        0,
                        false,
                        false,
                        false
                ),
                new PotionEffect(
                        PotionEffectType.REGENERATION,
                        PotionEffect.INFINITE_DURATION,
                        config.regenerationAmplifier,
                        false,
                        false,
                        false
                )
        );
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "azalea_camouflage");
    }

    @Override
    public long getCooldownTicks() {
        return config.cooldown;
    }

    @Override
    protected boolean canActivate(Player player) {
        Vector velocity = player.getVelocity();
        return velocity.getX() == 0 && velocity.getZ() == 0;
    }

    @Override
    @EventHandler
    public void handleToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        if (!toggleSneakAction(event, player)) return;

        BukkitScheduler scheduler = Bukkit.getScheduler();
        long taskDelay = config.activationTime + 1L;

        int sneakTime = player.getStatistic(Statistic.SNEAK_TIME);
        int requiredSneakTime = sneakTime + config.activationTime;

        scheduler.runTaskLater(JustRacesShowcase.INSTANCE, () -> {
            int newSneakTime = player.getStatistic(Statistic.SNEAK_TIME);

            if (newSneakTime < requiredSneakTime) return;
            tryActivate(player);
        }, taskDelay);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        clearCamoEffects(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        clearCamoEffects(event.getPlayer());
    }

    @EventHandler
    public void onEntityDismount(EntityDismountEvent event) {
        Entity entity = event.getEntity();

        if (!isCamoBlock(entity)) return;
        entity.remove();
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (!hasCamoBlock(player)) return;

        DamageType damageType = event.getDamageSource().getDamageType();

        if (BYPASSES_EFFECTS_VALUES.contains(damageType)) return;

        double damage = event.getDamage();
        double finalDamage = damage * config.damageMultiplier;

        event.setDamage(finalDamage);
    }

    @Override
    protected boolean onActivation(Player player, Object... ctx) {
        if (hasCamoBlock(player)) {
            removeCamoBlock(player);
            return true;
        }

        addCamoBlock(player);
        return false;
    }

    public void addCamoBlock(Player player) {
        if (hasCamoBlock(player)) return;

        // TODO: fix offset
        double playerHeight = player.getHeight();
        float playerFeet = (float) (playerHeight * -1);

        Transformation camoBlockTransformation = new Transformation(
                new Vector3f(-0.5f, playerFeet, -0.5f),
                new Quaternionf(),
                new Vector3f(1f,1f,1f),
                new Quaternionf()
        );

        BlockDisplay camoBlock = player.getWorld().spawn(
                player.getLocation(),
                BlockDisplay.class,
                CreatureSpawnEvent.SpawnReason.CUSTOM,
                blockDisplay -> {
                    blockDisplay.getPersistentDataContainer().set(getKey(), PersistentDataType.BOOLEAN, true);

                    blockDisplay.setBlock(Bukkit.createBlockData(Material.FLOWERING_AZALEA));

                    blockDisplay.setRotation(0, 0);
                    blockDisplay.setTransformation(camoBlockTransformation);
                }
        );

        player.addPassenger(camoBlock);
        giveCamoEffects(player);
    }

    private void giveCamoEffects(Player player) {
        userEffects.forEach(player::addPotionEffect);
        AttributeManager.addModifiers(player, camoAttributes);
    }

    private void clearCamoEffects(Player player) {
        userEffects.forEach(effect -> player.removePotionEffect(effect.getType()));
        AttributeManager.removeModifiers(player, camoAttributes);
    }

    public void removeCamoBlock(Player player) {
        player.getPassengers().forEach(passenger -> {
            PersistentDataContainerView pdc = passenger.getPersistentDataContainer();

            if (!pdc.has(getKey(), PersistentDataType.BOOLEAN)) return;

            passenger.remove();
        });

        clearCamoEffects(player);
    }

    public boolean hasCamoBlock(Player player) {
        List<Entity> passengers = player.getPassengers();
        boolean haveCamoBlock = false;

        if (!passengers.isEmpty()) {
            Entity passenger = passengers.getFirst();
            haveCamoBlock = isCamoBlock(passenger);
        }

        return haveCamoBlock;
    }

    private boolean isCamoBlock(Entity entity) {
        boolean isBlockDisplay = entity.getType() == EntityType.BLOCK_DISPLAY;
        boolean hasKey = entity.getPersistentDataContainer().has(getKey(), PersistentDataType.BOOLEAN);

        return isBlockDisplay && hasKey;
    }
}
