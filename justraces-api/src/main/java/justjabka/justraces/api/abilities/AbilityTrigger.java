package justjabka.justraces.api.abilities;

import justjabka.justraces.api.common.entry.AbilityEntry;

/**
 * The trigger is the general action used to trigger the ability.
 * <p>
 * Don't confuse ability trigger and ability activation requirements ({@code BaseAbility#canActivate(Player)}). Those are two different things
 * First one is to know that player actually wanted to activate ability.
 * Second one is to know if ability can actually work (player has some armor set equipped or have enough score)
 * @see AbilityTriggerCondition
 * @see AbilityEntry
 */
public enum AbilityTrigger {
    LEFT_CLICK,
    RIGHT_CLICK,

    SNEAK_ON,
    SNEAK_OFF,
    SNEAK_TOGGLE,

    JUMP,
    OFFHAND_SWAP,
    RIGHT_CLICK_CHESTPLATE,

    CUSTOM;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
