package justjabka.WeltenRaces.Abilities;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Equippable;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import justjabka.WeltenRaces.Abilities.Generic.BaseValidationAbility;
import justjabka.WeltenRaces.Configs.Ability.UnfoldWingsAbilityConfig;
import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.Runnables.Ability.UnfoldWingsAbilityRunnable;
import justjabka.WeltenRaces.WeltenRaces;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
public class UnfoldWingsAbility extends BaseValidationAbility {
    private final UnfoldWingsAbilityConfig config;
    private static final ItemStack WINGS_ITEM = createWings();

    public UnfoldWingsAbility(UnfoldWingsAbilityConfig config) {
        this.config = config;
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(WeltenRaces.NAMESPACE, "unfold_wings");
    }

    @Override
    public long getCooldownTicks() {
        return config.cooldown;
    }

    @EventHandler
    public void handleInteract(PlayerInteractEvent event) {
        super.handleInteract(event);
    }

    @Override
    protected boolean onActivation(Player player, Object... ctx) {
        if (haveItemInChestplateSlot(player)) return false;

        AttributeInstance jumpStrengthInstance = getJumpStrengthInstance(player);
        if (jumpStrengthInstance == null) return false;

        if (jumpStrengthInstance.getModifier(getKey()) != null) return false;

        jumpStrengthInstance.addModifier(
                new AttributeModifier(
                        getKey(),
                        config.jumpStrength,
                        AttributeModifier.Operation.ADD_NUMBER
                )
        );

        return true;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!AbilityManager.isAbilityActive(player, getKey())) return;

        onDeactivation(player);
    }

    @EventHandler
    public void onJump(PlayerJumpEvent event) {
        Player player = event.getPlayer();

        if (!raceHasAbility(player)) return;
        if (haveItemInChestplateSlot(player)) return;

        AttributeInstance jumpStrengthInstance = getJumpStrengthInstance(player);
        if (jumpStrengthInstance == null) return;

        if (jumpStrengthInstance.getModifier(getKey()) == null) return;

        giveWings(player, jumpStrengthInstance);
        onUseEffects(player);
    }

    @Override
    public boolean isStateValid(Player player) {
        return player.isGliding();
    }

    @Override
    public void onDeactivation(Player player) {
        removeWings(player);
    }

    /// Gives player ability to fly with wings
    public void giveWings(Player player, AttributeInstance jumpStrengthInstance) {
        if (haveItemInChestplateSlot(player)) return;

        AbilityManager.setAbilityState(player, getKey(), true);

        jumpStrengthInstance.removeModifier(getKey());

        changeWingsState(player, WINGS_ITEM);

        player.setGliding(true);
        new UnfoldWingsAbilityRunnable(this, player.getUniqueId()).runTaskTimer(WeltenRaces.INSTANCE, 10L, 2L);
    }

    private static boolean haveItemInChestplateSlot(Player player) {
        return !player.getEquipment().getChestplate().isEmpty();
    }

    /// Removes player's ability to fly with wings
    public void removeWings(Player player) {
        if (!player.getEquipment().getChestplate().isSimilar(WINGS_ITEM)) return;

        AbilityManager.setAbilityState(player, getKey(), false);
        changeWingsState(player, ItemStack.empty());
    }

    /// Changes wings item.
    /// Syncs equipment change with server and client
    private static void changeWingsState(Player player, ItemStack item) {
        player.getEquipment().setItem(EquipmentSlot.CHEST, item, true);
        player.sendEquipmentChange(player, EquipmentSlot.CHEST, item);
    }

    /// Creates wings item
    private static ItemStack createWings() {
        ItemStack wings = new ItemStack(
                Material.POISONOUS_POTATO
        );

        // 530000 IQ CRUTCH WILL LIVE IN OTHER BRANCHES FOREVER🗣️🔥🔥🔥
        Equippable equippable = Equippable
                .equippable(EquipmentSlot.CHEST)
                .swappable(false)
                .assetId(Key.key(WeltenRaces.NAMESPACE, "wings"))
                .build();
        wings.setData(DataComponentTypes.EQUIPPABLE, equippable);
        wings.setData(DataComponentTypes.GLIDER);

        wings.setData(DataComponentTypes.ITEM_NAME, Component.translatable("item.weltenraces.wings").fallback("Wings"));
        wings.setData(DataComponentTypes.ITEM_MODEL, new NamespacedKey(WeltenRaces.NAMESPACE, "wings"));
        wings.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay().addHiddenComponents(DataComponentTypes.ENCHANTMENTS).build());
        wings.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, false);

        wings.addUnsafeEnchantments(Map.of(
                Enchantment.BINDING_CURSE, 1,
                Enchantment.VANISHING_CURSE, 1
        ));

        return wings;
    }

    private static AttributeInstance getJumpStrengthInstance(Player player) {
        return player.getAttribute(Attribute.JUMP_STRENGTH);
    }

    private static void onUseEffects(Player player) {
        player.getWorld().spawnParticle(
                Particle.GUST_EMITTER_SMALL,
                player.getLocation(),
                1
        );
        player.getWorld().playSound(
                player.getLocation(),
                Sound.ENTITY_WIND_CHARGE_WIND_BURST,
                SoundCategory.PLAYERS,
                1,
                1
        );
    }
}
