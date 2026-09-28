package justjabka.justraces.api.traits.generic;

import justjabka.justraces.api.common.configurable.PluginConfigurable;

public interface ConfigurableTrait extends PluginConfigurable {

    @Override
    default Category category() {
        return Category.TRAITS;
    }

    default long getConfigTickPeriod() {
        return getConfigLong("period");
    }
}
