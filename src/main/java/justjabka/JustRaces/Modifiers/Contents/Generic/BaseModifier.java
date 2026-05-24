package justjabka.JustRaces.Modifiers.Contents.Generic;

import justjabka.JustRaces.Modifiers.ItemModifier;
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
    public abstract void apply(ItemStack item);

    @Override
    public abstract void undo(ItemStack item);
}
