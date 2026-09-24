package justjabka.justraces.api.managers;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Equippable;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NullMarked
public final class EffectManager {

    private EffectManager() {}

    private static final ItemStack GLIDER_FLIGHT_STACK;

    static {
        ItemStack elytra = new ItemStack(Material.POISONOUS_POTATO);

        elytra.setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(EquipmentSlot.SADDLE)
                .swappable(false)
                .build());
        elytra.setData(DataComponentTypes.GLIDER);

        elytra.addUnsafeEnchantments(Map.of(
                Enchantment.BINDING_CURSE, 1,
                Enchantment.VANISHING_CURSE, 1
        ));

        GLIDER_FLIGHT_STACK = elytra;
    }

    /**
     * Glows target for player on client side
     * @param player Player which client will receive packets
     * @param entity Entity that will be glowing
     * @param glow {@code true} to enable glow. {@code false} to disable it
     */
    public static void setClientSideGlow(Player player, Entity entity, boolean glow) {
        // Get entity metadata
        List<EntityData<?>> metadata = new ArrayList<>(
                SpigotConversionUtil.getEntityMetadata(entity)
        );

        // Check for existing value with index of 0
        byte currentMask = 0;
        EntityData<?> existingFlagData = null;

        for (EntityData<?> data : metadata) {
            if (data.getIndex() != 0) continue;
            if (!(data.getValue() instanceof Byte b)) continue;

            currentMask = b;
            existingFlagData = data;
            break;
        }

        // Change glow bit
        if (glow) {
            currentMask |= 0x40;
        } else {
            currentMask &= ~0x40;
        }

        if (existingFlagData != null) {
            metadata.remove(existingFlagData);
        }
        metadata.add(new EntityData<>(0, EntityDataTypes.BYTE, currentMask));

        // Send packet
        WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(
                entity.getEntityId(),
                metadata
        );

        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

    public static void setGliderFlight(Player player, boolean gliderFlight) {
        ItemStack glider;

        if (gliderFlight) {
            glider = GLIDER_FLIGHT_STACK;
        } else {
            glider = ItemStack.empty();
        }

        player.getEquipment().setItem(EquipmentSlot.SADDLE, glider);
        player.sendEquipmentChange(player, EquipmentSlot.SADDLE, glider);
    }
}
