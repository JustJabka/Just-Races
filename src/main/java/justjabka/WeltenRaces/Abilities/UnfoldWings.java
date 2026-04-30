package justjabka.WeltenRaces.Abilities;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Equippable;
import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.Configs.Abilities.UnfoldWingsConfig;
import justjabka.WeltenRaces.Managers.RaceManager;
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
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
public class UnfoldWings extends BaseAbility {
    UnfoldWingsConfig config;

    public UnfoldWings(UnfoldWingsConfig config) {
        this.config = config;
    }

    NamespacedKey UNFOLD_WINGS = new NamespacedKey(WeltenRaces.NAMESPACE, "unfold_wings");

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        super.handleInteract(event);
    }

    @Override
    public long getCooldownTicks() {
        return config.cooldown;
    }

    @Override
    public String getDisplayName() {
        return "Unfold Wings";
    }

    @Override
    protected boolean canActivate(Player player) {
        return RaceManager.getRace(player) == Race.PHANTOM;
    }

    @Override
    protected boolean onActivation(Player player) {
        AttributeInstance jumpStrengthInstance = getJumpStrengthInstance(player);
        if (jumpStrengthInstance == null) return false;

        if (jumpStrengthInstance.getModifier(UNFOLD_WINGS) != null) return false;
        jumpStrengthInstance.addModifier(
                new AttributeModifier(
                        UNFOLD_WINGS,
                        config.jumpStrength,
                        AttributeModifier.Operation.ADD_NUMBER
                )
        );

        return true;
    }

    @EventHandler
    public void onJump(PlayerJumpEvent event) {
        Player player = event.getPlayer();

        if (RaceManager.getRace(player) != Race.PHANTOM) return;

        AttributeInstance jumpStrengthInstance = getJumpStrengthInstance(player);
        if (jumpStrengthInstance == null) return;

        if (jumpStrengthInstance.getModifier(UNFOLD_WINGS) == null) return;
        jumpStrengthInstance.removeModifier(UNFOLD_WINGS);

        onUseEffects(player);

        ItemStack wingsItem = createWings();
        updateWingsState(player, wingsItem);

        startLandingTask(player);
    }

    private void startLandingTask(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                boolean flightEnded = !player.isOnline() || player.isOnGround();

                if (!flightEnded) return;

                updateWingsState(player, ItemStack.empty());
                this.cancel();
            }
        }.runTaskTimer(WeltenRaces.INSTANCE, 10L, 2L);
    }

    private static void updateWingsState(Player player, ItemStack item) {
        player.getEquipment().setItem(EquipmentSlot.SADDLE, item, true);
        player.sendEquipmentChange(player, EquipmentSlot.SADDLE, item);
    }

    private ItemStack createWings() {
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

    private static AttributeInstance getJumpStrengthInstance(Player player) {
        return player.getAttribute(Attribute.JUMP_STRENGTH);
    }
}
