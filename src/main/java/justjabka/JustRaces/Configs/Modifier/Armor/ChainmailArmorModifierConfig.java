package justjabka.JustRaces.Configs.Modifier.Armor;

import org.bukkit.configuration.file.FileConfiguration;

public class ChainmailArmorModifierConfig {
    public final double attributeAmount;

    public ChainmailArmorModifierConfig(FileConfiguration config) {
        this.attributeAmount = config.getDouble("attribute-amount", 1);
    }
}
