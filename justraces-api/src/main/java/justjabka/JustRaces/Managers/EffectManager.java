package justjabka.JustRaces.Managers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EffectManager {
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
    public static void setClientSideGlow(Player player, LivingEntity entity, boolean glow) {
        // Magic packets✨ (idk how ts magic shit works💀)
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        PacketContainer packet = manager.createPacket(PacketType.Play.Server.ENTITY_METADATA);

        packet.getIntegers().write(0, entity.getEntityId());

        byte currentMask = 0;

        WrappedDataWatcher watcher = WrappedDataWatcher.getEntityWatcher(entity);
        if (watcher.hasIndex(0)) {
            currentMask = (byte) watcher.getObject(0);
        }

        if (glow) {
            currentMask |= 0x40;
        } else {
            currentMask &= ~0x40;
        }

        List<WrappedDataValue> dataValues = new ArrayList<>();

        dataValues.add(new WrappedDataValue(
                0,
                WrappedDataWatcher.Registry.get((Type) Byte.class),
                currentMask
        ));

        packet.getDataValueCollectionModifier().write(0, dataValues);

        manager.sendServerPacket(player, packet);
    }
}
