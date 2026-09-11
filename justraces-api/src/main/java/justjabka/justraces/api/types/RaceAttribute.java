package justjabka.justraces.api.types;

import justjabka.justraces.api.JustRacesAPI;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

import java.util.Optional;

public record RaceAttribute(
        Attribute attribute,
        double amount,
        Optional<AttributeModifier.Operation> operation
) {
    public boolean isModifier() {
        return operation.isPresent();
    }

    public boolean isBaseValue() {
        return operation.isEmpty();
    }

    public AttributeModifier createModifier(NamespacedKey raceKey) {
        if (operation.isEmpty()) {
            throw new IllegalStateException("Cannot create AttributeModifier for %s".formatted(attribute.getKey()));
        }

        NamespacedKey attributeKey = new NamespacedKey(
                JustRacesAPI.NAMESPACE,
                "race.%s.%s".formatted(raceKey.getNamespace(), raceKey.getKey())
        );

        return new AttributeModifier(attributeKey, amount, operation.get());
    }
}
