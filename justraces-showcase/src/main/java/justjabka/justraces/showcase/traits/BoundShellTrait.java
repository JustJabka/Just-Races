package justjabka.justraces.showcase.traits;

import io.papermc.paper.event.entity.EntityEquipmentChangedEvent;
import justjabka.justraces.api.traits.generic.BaseTraitListener;
import justjabka.justraces.api.traits.generic.ResettableTrait;
import justjabka.justraces.showcase.JustRacesShowcase;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.UUID;

public class BoundShellTrait extends BaseTraitListener implements ResettableTrait {

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "bound_shell");
    }

    @EventHandler(ignoreCancelled = true)
    public void onArmorChange(EntityEquipmentChangedEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!isRequiredTrait(player)) return;

        applyBoundShellBonus(player);
    }

    @Override
    public void applyState(Player player) {
        applyBoundShellBonus(player);
    }

    @Override
    public void resetState(UUID pid, Reason reason) {
        Player player = Bukkit.getPlayer(pid);
        if (player == null) return;

        AttributeInstance maxHealthInstance = player.getAttribute(Attribute.MAX_HEALTH);
        if (maxHealthInstance == null) return;

        maxHealthInstance.removeModifier(getKey());
    }

    private void applyBoundShellBonus(Player player) {
        // Get Attributes
        AttributeInstance maxHealthInstance = player.getAttribute(Attribute.MAX_HEALTH);
        AttributeInstance armorInstance = player.getAttribute(Attribute.ARMOR);

        if (maxHealthInstance == null || armorInstance == null) return;

        double armorValue = armorInstance.getValue();

        // Delete old attribute
        maxHealthInstance.removeModifier(getKey());

        // Calc new attribute
        if (armorValue <= 0) return;

        AttributeModifier modifier = new AttributeModifier(
                getKey(),
                armorValue,
                AttributeModifier.Operation.ADD_NUMBER
        );
        maxHealthInstance.addModifier(modifier);
    }
}
