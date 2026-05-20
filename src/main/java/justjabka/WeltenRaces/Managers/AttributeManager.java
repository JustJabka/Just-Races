package justjabka.WeltenRaces.Managers;

import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

import java.util.Map;

public class AttributeManager {
    /**
     * Applies attribute modifiers to player
     * @param player Player to which attributes modifiers will be applied
     * @param modifiers Attribute modifiers that will be applied
     * @see #removeModifiers(Player, Map) 
     */
    public static void addModifiers(Player player, Map<Attribute, AttributeModifier> modifiers) {
        modifiers.forEach((attribute, attributeModifier) -> {
            AttributeInstance instance = player.getAttribute(attribute);

            if (instance == null) return;

            instance.addModifier(attributeModifier);
        });
    }

    /**
     * Removes player's applied attribute modifiers
     * @param player Player whose attribute modifiers will be removed
     * @param modifiers Attribute modifiers that will be removed
     * @see #addModifiers(Player, Map) 
     */
    public static void removeModifiers(Player player, Map<Attribute, AttributeModifier> modifiers) {
        modifiers.forEach((attribute, attributeModifier) -> {
            AttributeInstance instance = player.getAttribute(attribute);

            if (instance == null) return;

            instance.removeModifier(attributeModifier);
        });
    }

    /**
     * Checks if player has applied attribute modifiers
     * @param player Player whose attribute modifiers will be checked
     * @param modifiers Attribute modifiers that will be searched
     * @return {@code true} if attribute modifiers was found
     */
    public static boolean hasModifiers(Player player, Map<Attribute, AttributeModifier> modifiers) {
        return modifiers.entrySet().stream().allMatch(entry -> {
            AttributeInstance instance = player.getAttribute(entry.getKey());

            NamespacedKey modifierKey = entry.getValue().getKey();

            return instance != null && instance.getModifier(modifierKey) != null;
        });
    }

    /**
     * Sets base value of the attribute
     * @param player Player whose base attribute value will be changed
     * @param attribute Attribute that will be changed
     * @param value Value of and attribute
     * @see #resetBaseValue(Player, Attribute) 
     */
    public static void setBaseValue(Player player, Attribute attribute, double value) {
        AttributeInstance instance = player.getAttribute(attribute);

        if (instance == null) return;

        instance.setBaseValue(value);
    }

    /**
     * Resets base attribute value
     * @param player Player whose base attribute will be reseted
     * @param attribute Attribute that will be reseted
     * @see #setBaseValue(Player, Attribute, double) 
     */
    public static void resetBaseValue(Player player, Attribute attribute) {
        Attributable defaultAttributes = player.getType().getDefaultAttributes();

        AttributeInstance instance = player.getAttribute(attribute);
        AttributeInstance defaultInstance = defaultAttributes.getAttribute(attribute);

        if (instance == null) return;
        if (defaultInstance == null) return;

        instance.setBaseValue(defaultInstance.getBaseValue());
    }

    /**
     * Removes all attribute modifiers with namespace of {@code WeltenRaces.NAMESPACE}
     * @param player Player whose attribute modifiers will be removed
     * @apiNote Do not confuse with {@link #removeModifiers(Player, Map)}
     */
    public static void removeAllModifiers(Player player) {
        for (Attribute attribute : Registry.ATTRIBUTE) {
            AttributeInstance instance = player.getAttribute(attribute);

            if (instance == null) continue;

            for (AttributeModifier modifier : instance.getModifiers()) {
                String modifierNamespace = modifier.getKey().getNamespace();

                if (!modifierNamespace.equals(WeltenRaces.NAMESPACE)) continue;

                instance.removeModifier(modifier);
            }
        }
    }
}
