package justjabka.justraces.api.managers;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public final class EffectManager {

    private EffectManager() {}

    public static double calcAbsorptionAmountFromConsumable(Consumable consumable) {
        for (ConsumeEffect effect : consumable.consumeEffects()) {
            if (!(effect instanceof ConsumeEffect.ApplyStatusEffects applyEffect)) continue;

            for (PotionEffect potionEffect : applyEffect.effects()) {
                if (!(potionEffect.getType().equals(PotionEffectType.ABSORPTION))) continue;
                return (potionEffect.getAmplifier() + 1) * 4.0;
            }
        }
        return 0;
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
}
