package justjabka.WeltenRaces.Abilities;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Equippable;
import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.Configs.Abilities.UnfoldWingsConfig;
import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.Runnables.UnfoldWingsAbilityRunnable;
import justjabka.WeltenRaces.Types.Race;
import justjabka.WeltenRaces.WeltenRaces;
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
public class UnfoldWings extends BaseAbility {
    UnfoldWingsConfig config;

    public UnfoldWings(UnfoldWingsConfig config) {
        this.config = config;
    }

    public static final NamespacedKey UNFOLD_WINGS_KEY = new NamespacedKey(WeltenRaces.NAMESPACE, "unfold_wings");

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        super.handleInteract(event);
    }

    @Override
    public long getCooldownTicks() {
        return config.cooldown;
    }

    @Override
    protected boolean onActivation(Player player) {
        AttributeInstance jumpStrengthInstance = getJumpStrengthInstance(player);
        if (jumpStrengthInstance == null) return false;

        if (jumpStrengthInstance.getModifier(UNFOLD_WINGS_KEY) != null) return false;

        jumpStrengthInstance.addModifier(
                new AttributeModifier(
                        UNFOLD_WINGS_KEY,
                        config.jumpStrength,
                        AttributeModifier.Operation.ADD_NUMBER
                )
        );

        return true;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!AbilityManager.isAbilityActive(player, UNFOLD_WINGS_KEY)) return;

        removeWings(player);
    }

    @EventHandler
    public void onJump(PlayerJumpEvent event) {
        Player player = event.getPlayer();

        if (RaceManager.getRace(player) != Race.PHANTOM) return;

        AttributeInstance jumpStrengthInstance = getJumpStrengthInstance(player);
        if (jumpStrengthInstance == null) return;

        if (jumpStrengthInstance.getModifier(UNFOLD_WINGS_KEY) == null) return;

        giveWings(player, jumpStrengthInstance);
        onUseEffects(player);
    }

    /// Gives player ability to fly with wings
    public static void giveWings(Player player, AttributeInstance jumpStrengthInstance) {
        // TODO: Add visual wings

        AbilityManager.changeAbilityState(player, UNFOLD_WINGS_KEY, true);

        jumpStrengthInstance.removeModifier(UNFOLD_WINGS_KEY);

        ItemStack wingsItem = createWings();
        changeWingsState(player, wingsItem);

        player.setGliding(true);
        new UnfoldWingsAbilityRunnable(player.getUniqueId()).runTaskTimer(WeltenRaces.INSTANCE, 10L, 2L);
    }

    /// Removes player's ability to fly with wings
    public static void removeWings(Player player) {
        AbilityManager.changeAbilityState(player, UNFOLD_WINGS_KEY, false);
        changeWingsState(player, ItemStack.empty());
    }

    /// Changes wings item.
    /// Syncs equipment change with server and client
    private static void changeWingsState(Player player, ItemStack item) {
        player.getEquipment().setItem(EquipmentSlot.SADDLE, item, true);
        player.sendEquipmentChange(player, EquipmentSlot.SADDLE, item);
    }

    /// Creates wings item
    private static ItemStack createWings() {
        ItemStack wings = new ItemStack(
                Material.POISONOUS_POTATO
        );

        // 530000 IQ CRUTCH🧠
        Equippable equippable = Equippable
                .equippable(EquipmentSlot.SADDLE)
                .swappable(false)
                .build();
        wings.setData(DataComponentTypes.EQUIPPABLE, equippable);
        wings.setData(DataComponentTypes.GLIDER);

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
