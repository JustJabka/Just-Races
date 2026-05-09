package justjabka.WeltenRaces.Modifiers;

import org.bukkit.inventory.ItemStack;

public interface ItemModifier {
    void apply(ItemStack item);
    void undo(ItemStack item);

    String getId();
}