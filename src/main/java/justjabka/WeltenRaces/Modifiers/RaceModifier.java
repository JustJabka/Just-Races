package justjabka.WeltenRaces.Modifiers;

import org.bukkit.inventory.ItemStack;

public interface RaceModifier {
    void apply(ItemStack item);
    void undo(ItemStack item);
}