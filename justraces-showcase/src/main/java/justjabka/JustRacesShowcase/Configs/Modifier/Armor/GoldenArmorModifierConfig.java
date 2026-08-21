package justjabka.JustRacesShowcase.Configs.Modifier.Armor;

import org.bukkit.configuration.file.FileConfiguration;

public class GoldenArmorModifierConfig {
    public final double attributeAmount;

    public GoldenArmorModifierConfig(FileConfiguration config) {
        this.attributeAmount = config.getDouble("attribute-amount", 1);
    }
}
