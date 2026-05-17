package justjabka.WeltenRaces.Modifiers;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public interface ItemModifier {
    void apply(ItemStack item);
    void undo(ItemStack item);

    NamespacedKey getKey();
}