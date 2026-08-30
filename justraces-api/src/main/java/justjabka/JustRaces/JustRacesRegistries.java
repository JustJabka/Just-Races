package justjabka.JustRaces;

import justjabka.JustRaces.Abilities.Generic.BaseAbility;
import justjabka.JustRaces.Instances.RaceInstance;
import justjabka.JustRaces.Modifiers.Generic.BaseModifier;

public class JustRacesRegistries {
    public static final Registry<RaceInstance> RACES = new Registry<>();
    public static final Registry<BaseAbility> ABILITIES = new Registry<>();
    public static final Registry<BaseModifier> MODIFIERS = new Registry<>();
}
