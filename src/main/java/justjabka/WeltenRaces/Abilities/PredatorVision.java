package justjabka.WeltenRaces.Abilities;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import justjabka.WeltenRaces.Abilities.Generic.BaseAbility;
import justjabka.WeltenRaces.Configs.Abilities.PredatorVisionConfig;
import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.Types.Race;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.Bukkit;
import org.bukkit.Tag;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PredatorVision extends BaseAbility {
    PredatorVisionConfig config;

    public PredatorVision(PredatorVisionConfig config) {
        this.config = config;
    }

    List<LivingEntity> markedVictims = new ArrayList<>();

    @Override
    public long getCooldownTicks() {
        return config.cooldown;
    }

    @Override
    public String getDisplayName() {
        return "Predator Vision";
    }

    @Override
    protected boolean canActivate(Player player) {
        if (RaceManager.getRace(player) != Race.PHANTOM) return false;
        return true;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        super.handleInteract(event);
    }

    @Override
    protected boolean onActivation(Player player) {
        // Apply glow
        for (LivingEntity victim : player.getLocation().getNearbyLivingEntities(10)) {
            if (victim == player) continue;

            EntityType victimType = victim.getType();
            boolean isUndead = Tag.ENTITY_TYPES_UNDEAD.getValues().contains(victimType);

            if (isUndead) continue;

            setGlowing(player, victim, true);
            markedVictims.add(victim);
        }

        // Remove glow
        Bukkit.getScheduler().runTaskLater(WeltenRaces.INSTANCE, () -> {
            if (!(player.isOnline())) return;

            for (LivingEntity victim : markedVictims) {
                if (!(victim.isValid())) continue;

                setGlowing(player, victim, false);
            }
        }, config.effectDuration);

        return true;
    }

    private static void setGlowing(Player send, LivingEntity glowing, boolean glow) {
        // Magic packets✨ (idk how ts magic shit works💀)
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        PacketContainer packet = manager.createPacket(PacketType.Play.Server.ENTITY_METADATA);

        packet.getIntegers().write(0, glowing.getEntityId());

        byte currentMask = 0;

        WrappedDataWatcher watcher = WrappedDataWatcher.getEntityWatcher(glowing);
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

        manager.sendServerPacket(send, packet);
    }

    @Override
    protected boolean activateAction(PlayerInteractEvent event, Player player) {
        if (event.getHand() == EquipmentSlot.OFF_HAND) return false;
        if (!event.getAction().isRightClick()) return false;
        if (!player.isSneaking()) return false;

        return true;
    }
}
