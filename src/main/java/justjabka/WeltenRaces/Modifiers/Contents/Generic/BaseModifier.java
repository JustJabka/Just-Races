package justjabka.WeltenRaces.Modifiers.Contents.Generic;

import justjabka.WeltenRaces.Modifiers.ItemModifier;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public abstract class BaseModifier implements ItemModifier {
    private final NamespacedKey key;

    public BaseModifier(NamespacedKey key) {
        this.key = key;
    }

    @Override
    public NamespacedKey getKey() {
        return key;
    }

    @Override
    public void apply(ItemStack item) {}

    @Override
    public void undo(ItemStack item) {}
}
