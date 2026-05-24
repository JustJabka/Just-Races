package justjabka.JustRaces.Modifiers;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public interface ItemModifier {
    NamespacedKey getKey();

    void apply(ItemStack item);
    void undo(ItemStack item);
}