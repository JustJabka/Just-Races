package justjabka.justraces.api.interfaces.configurable;

import justjabka.justraces.api.interfaces.configurable.generic.PluginConfigurable;

public interface ItemModifierConfigurable extends PluginConfigurable {
    @Override
    default Category getCategory() {
        return Category.MODIFIERS;
    }
}
