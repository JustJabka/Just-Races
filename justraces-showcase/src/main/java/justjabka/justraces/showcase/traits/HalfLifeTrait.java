package justjabka.justraces.showcase.traits;

import justjabka.justraces.api.JustRacesRegistries;
import justjabka.justraces.api.managers.AbilityManager;
import justjabka.justraces.api.managers.HealthManager;
import justjabka.justraces.api.managers.TransientManager;
import justjabka.justraces.api.traits.generic.BaseTraitRunnable;
import justjabka.justraces.api.traits.generic.ConfigurableTrait;
import justjabka.justraces.api.traits.generic.ResettableTrait;
import justjabka.justraces.showcase.JustRacesShowcase;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

import java.util.*;

public class HalfLifeTrait extends BaseTraitRunnable implements ResettableTrait, ConfigurableTrait {
    private static final Random RANDOM = new Random();

    // I'm not moving ts to config😭
    private static final Set<UnstableAttributeModifier> UNSTABLE_ATTRIBUTE_MODIFIERS = Set.of(
            new UnstableAttributeModifier(Attribute.MAX_HEALTH, -5, 15),
            new UnstableAttributeModifier(Attribute.ARMOR, -2, 6),
            new UnstableAttributeModifier(Attribute.ARMOR_TOUGHNESS, -1, 4),
            new UnstableAttributeModifier(Attribute.KNOCKBACK_RESISTANCE, 0, 0.25),

            new UnstableAttributeModifier(Attribute.ATTACK_DAMAGE, -0.5, 5),
            new UnstableAttributeModifier(Attribute.ATTACK_SPEED, -0.5, 1),
            new UnstableAttributeModifier(Attribute.ATTACK_KNOCKBACK, -0.25, 1),

            new UnstableAttributeModifier(Attribute.BLOCK_INTERACTION_RANGE, -0.5, 3),
            new UnstableAttributeModifier(Attribute.ENTITY_INTERACTION_RANGE, -0.5, 2),

            new UnstableAttributeModifier(Attribute.MOVEMENT_SPEED, -0.015, 0.03),
            new UnstableAttributeModifier(Attribute.WATER_MOVEMENT_EFFICIENCY, 0, 1),
            new UnstableAttributeModifier(Attribute.SAFE_FALL_DISTANCE, -1, 7),
            new UnstableAttributeModifier(Attribute.FALL_DAMAGE_MULTIPLIER, -0.8, 0.3),
            new UnstableAttributeModifier(Attribute.STEP_HEIGHT, -0.1, 1),

            new UnstableAttributeModifier(Attribute.BURNING_TIME, -0.25, 0.1),
            new UnstableAttributeModifier(Attribute.OXYGEN_BONUS, 0, 2)
    );

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "half_life");
    }

    @Override
    public long getTickPeriod() {
        return getConfigTickPeriod();
    }

    @Override
    public void onTick(Player player) {
        rerollEverything(player);
    }

    @Override
    public void applyState(Player player) {
        rerollEverything(player);
    }

    @Override
    public void resetState(UUID pid, Reason reason) {
        Player player = Bukkit.getPlayer(pid);
        if (player == null) return;

        removeUnstableAttributes(player);
    }

    private void rerollEverything(Player player) {
        rerollAbilitiesAndTraits(player);
        addUnstableAttributes(player);

        Bukkit.getScheduler().runTask(JustRacesShowcase.INSTANCE, () -> revealActiveAbilities(player));
        onUseEffects(player);
    }

    private void rerollAbilitiesAndTraits(Player player) {
        final int maxAbilities = getConfigInt("max_abilities");
        final int maxTraits = getConfigInt("max_traits");

        getRandomElements(JustRacesRegistries.ABILITIES.values(), maxAbilities)
                .forEach(ability ->
                        TransientManager.addTransientAbility(player, ability, getConfigTickPeriod())
                );

        getRandomElements(JustRacesRegistries.TRAITS.values(), maxTraits)
                .forEach(trait ->
                        TransientManager.addTransientTrait(player, trait, getConfigTickPeriod())
                );
    }

    private static void revealActiveAbilities(Player player) {
        List<Component> abilities = AbilityManager.getAbilitiesForPlayer(player).stream()
                .map(ability -> ability.getCooldownBarIcon(player))
                .toList();

        Component message = Component.join(
                JoinConfiguration.separator(Component.space()),
                abilities
        );

        player.sendActionBar(message);
    }

    private void addUnstableAttributes(Player player) {
        double healthPercent = HealthManager.getCurrentHealthPercent(player);

        for (UnstableAttributeModifier modifier : UNSTABLE_ATTRIBUTE_MODIFIERS) {
            Attribute attribute = modifier.attribute();

            AttributeInstance instance = player.getAttribute(attribute);
            if (instance == null) continue;

            double amount = RANDOM.nextDouble(modifier.minAmount(), modifier.maxAmount());

            instance.removeModifier(getKey());
            instance.addModifier(new AttributeModifier(
                    getKey(),
                    amount,
                    AttributeModifier.Operation.ADD_NUMBER)
            );
        }

        HealthManager.setHealthPercent(player, healthPercent);
    }

    private void removeUnstableAttributes(Player player) {
        double healthPercent = HealthManager.getCurrentHealthPercent(player);

        for (UnstableAttributeModifier modifier : UNSTABLE_ATTRIBUTE_MODIFIERS) {
            Attribute attribute = modifier.attribute();

            AttributeInstance instance = player.getAttribute(attribute);
            if (instance == null) continue;

            instance.removeModifier(getKey());
        }

        HealthManager.setHealthPercent(player, healthPercent);
    }

    private static void onUseEffects(Player player) {
        World world = player.getWorld();
        world.playSound(player.getLocation(), Sound.AMBIENT_BASALT_DELTAS_ADDITIONS, SoundCategory.PLAYERS, 2, 1);
        world.spawnParticle(
                Particle.COPPER_FIRE_FLAME,
                player.getEyeLocation().subtract(0, 0.5, 0),
                10,
                0.25,
                0.5,
                0.25,
                0.02
        );
    }

    private <T> Set<T> getRandomElements(Collection<T> collection, int maxSize) {
        List<T> list = new ArrayList<>(collection.stream()
                .filter(e -> !e.equals(this))
                .toList());

        if (list.isEmpty()) return Collections.emptySet();

        Collections.shuffle(list, RANDOM);
        return new HashSet<>(list.subList(0, Math.min(maxSize, list.size())));
    }

    private record UnstableAttributeModifier(Attribute attribute, double minAmount, double maxAmount) {}
}
