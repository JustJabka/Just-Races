package justjabka.justraces.api.itemmodifiers.generic;

import justjabka.justraces.api.common.configurable.PluginConfigurable;

public interface ConfigurableItemModifier extends PluginConfigurable {

    @Override
    default Category category() {
        return Category.ITEM_MODIFIERS;
    }
}
