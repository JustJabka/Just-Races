package justjabka.justraces.api.traits.generic;

import justjabka.justraces.api.common.configurable.PluginConfigurable;

public interface ConfigurableTrait extends PluginConfigurable {
    @Override
    default Category getCategory() {
        return Category.TRAITS;
    }
}
