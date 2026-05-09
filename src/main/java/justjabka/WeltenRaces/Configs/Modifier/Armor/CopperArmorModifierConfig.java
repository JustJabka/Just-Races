package justjabka.WeltenRaces.Configs.Modifier.Armor;

import org.bukkit.configuration.file.FileConfiguration;

public class CopperArmorModifierConfig {
    public final double attributeAmount;

    public CopperArmorModifierConfig(FileConfiguration config) {
        this.attributeAmount = config.getDouble("attribute-amount", 0.5);
    }
}
