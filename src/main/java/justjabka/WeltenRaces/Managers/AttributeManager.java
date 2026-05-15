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
    public static void addModifiers(Player player, Map<Attribute, AttributeModifier> modifiers) {
        modifiers.forEach((attribute, attributeModifier) -> {
            AttributeInstance instance = player.getAttribute(attribute);

            if (instance == null) return;

            instance.addModifier(attributeModifier);
        });
    }

    public static void removeModifiers(Player player, Map<Attribute, AttributeModifier> modifiers) {
        modifiers.forEach((attribute, attributeModifier) -> {
            AttributeInstance instance = player.getAttribute(attribute);

            if (instance == null) return;

            instance.removeModifier(attributeModifier);
        });
    }

    public static boolean hasModifiers(Player player, Map<Attribute, AttributeModifier> modifiers) {
        return modifiers.entrySet().stream().allMatch(entry -> {
            AttributeInstance instance = player.getAttribute(entry.getKey());

            NamespacedKey modifierKey = entry.getValue().getKey();

            return instance != null && instance.getModifier(modifierKey) != null;
        });
    }

    public static void setBaseValue(Player player, Attribute attribute, double value) {
        AttributeInstance instance = player.getAttribute(attribute);

        if (instance == null) return;

        instance.setBaseValue(value);
    }

    public static void resetBaseValue(Player player, Attribute attribute) {
        Attributable defaultAttributes = player.getType().getDefaultAttributes();

        AttributeInstance instance = player.getAttribute(attribute);
        AttributeInstance defaultInstance = defaultAttributes.getAttribute(attribute);

        if (instance == null) return;
        if (defaultInstance == null) return;

        instance.setBaseValue(defaultInstance.getBaseValue());
    }

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
