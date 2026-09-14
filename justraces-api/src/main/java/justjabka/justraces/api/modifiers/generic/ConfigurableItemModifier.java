package justjabka.justraces.api.modifiers.generic;

import justjabka.justraces.api.common.configurable.PluginConfigurable;

public interface ConfigurableItemModifier extends PluginConfigurable {
    @Override
    default Category getCategory() {
        return Category.MODIFIERS;
    }
}
