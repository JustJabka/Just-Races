package justjabka.WeltenRaces.Abilities;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Equippable;
import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.Configs.Ability.UnfoldWingsAbilityConfig;
import justjabka.WeltenRaces.Managers.AbilityManager;
import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.Runnables.Ability.UnfoldWingsAbilityRunnable;
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
public class UnfoldWingsAbility extends BaseAbility {
    UnfoldWingsAbilityConfig config;

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
    public void onInteract(PlayerInteractEvent event) {
        super.handleInteract(event);
    }

    @Override
    protected boolean onActivation(Player player) {
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

        if (RaceManager.getRace(player) != Race.PHANTOM) return;

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
        // TODO: Add visual wings
        AbilityManager.changeAbilityState(player, getKey(), true);

        jumpStrengthInstance.removeModifier(getKey());

        ItemStack wingsItem = createWings();
        changeWingsState(player, wingsItem);

        player.setGliding(true);
        new UnfoldWingsAbilityRunnable(this, player.getUniqueId()).runTaskTimer(WeltenRaces.INSTANCE, 10L, 2L);
    }

    /// Removes player's ability to fly with wings
    public void removeWings(Player player) {
        AbilityManager.changeAbilityState(player, getKey(), false);
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
