package justjabka.JustRaces.Configs.Modifier.Armor;

import org.bukkit.configuration.file.FileConfiguration;

public class LeatherArmorModifierConfig {
    public final double attributeAmount;

    public LeatherArmorModifierConfig(FileConfiguration config) {
        this.attributeAmount = config.getDouble("attribute-amount", 0.01);
    }
}
