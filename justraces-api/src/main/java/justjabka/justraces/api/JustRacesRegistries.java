package justjabka.justraces.api;

import justjabka.justraces.api.abilities.generic.BaseAbility;
import justjabka.justraces.api.definitions.RaceDefinition;
import justjabka.justraces.api.interfaces.Registry;
import justjabka.justraces.api.interfaces.Trait;
import justjabka.justraces.api.modifiers.generic.BaseModifier;

public final class JustRacesRegistries {
    public static Registry<RaceDefinition> RACES;
    public static Registry<BaseAbility> ABILITIES;
    public static Registry<BaseModifier> MODIFIERS;
    public static Registry<Trait> TRAITS;

    private JustRacesRegistries() {}
}
