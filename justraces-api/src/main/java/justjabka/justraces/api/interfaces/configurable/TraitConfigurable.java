package justjabka.justraces.api.interfaces.configurable;

import justjabka.justraces.api.interfaces.configurable.generic.PluginConfigurable;

public interface TraitConfigurable extends PluginConfigurable {
    @Override
    default Category getCategory() {
        return Category.TRAITS;
    }
}
