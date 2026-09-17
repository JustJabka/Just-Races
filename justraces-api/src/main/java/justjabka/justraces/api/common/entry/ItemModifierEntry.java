package justjabka.justraces.api.common.entry;

import justjabka.justraces.api.itemmodifiers.generic.BaseItemModifier;
import org.bukkit.Material;

import java.util.Set;

public record ItemModifierEntry(
        BaseItemModifier modifier,
        Set<Material> materials
) {
}
