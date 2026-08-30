package justjabka.JustRaces.Interfaces.Configurable;

import justjabka.JustRaces.Interfaces.Configurable.Generic.PluginConfigurable;

public interface ItemModifierConfigurable extends PluginConfigurable {
    @Override
    default Category getCategory() {
        return Category.MODIFIERS;
    }
}
