package justjabka.justraces.api;

import justjabka.justraces.api.abilities.generic.BaseAbility;
import justjabka.justraces.api.races.RaceDefinition;
import justjabka.justraces.api.common.registry.Registry;
import justjabka.justraces.api.traits.generic.Trait;
import justjabka.justraces.api.itemmodifiers.generic.BaseItemModifier;

public final class JustRacesRegistries {
    public static Registry<RaceDefinition> RACES;
    public static Registry<BaseAbility> ABILITIES;
    public static Registry<BaseItemModifier> ITEM_MODIFIERS;
    public static Registry<Trait> TRAITS;

    private JustRacesRegistries() {}
}
