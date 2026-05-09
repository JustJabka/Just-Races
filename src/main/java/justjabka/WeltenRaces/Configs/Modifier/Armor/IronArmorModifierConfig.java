package justjabka.WeltenRaces.Configs.Modifier.Armor;

import org.bukkit.configuration.file.FileConfiguration;

public class IronArmorModifierConfig {
    public final double attributeAmount;

    public IronArmorModifierConfig(FileConfiguration config) {
        this.attributeAmount = config.getDouble("attribute-amount", 0.5);
    }
}
