package justjabka.justraces.api;

import justjabka.justraces.api.abilities.generic.BaseAbility;
import justjabka.justraces.api.definitions.RaceDefinition;
import justjabka.justraces.api.interfaces.Trait;
import justjabka.justraces.api.modifiers.generic.BaseModifier;

public class JustRacesRegistries {
    public static final Registry<RaceDefinition> RACES = new Registry<>();
    public static final Registry<BaseAbility> ABILITIES = new Registry<>();
    public static final Registry<BaseModifier> MODIFIERS = new Registry<>();
    public static final Registry<Trait> TRAITS = new Registry<>();
}
