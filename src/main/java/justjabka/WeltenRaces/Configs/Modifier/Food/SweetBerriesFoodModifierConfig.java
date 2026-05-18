package justjabka.WeltenRaces.Configs.Modifier.Food;

import org.bukkit.configuration.file.FileConfiguration;

public class SweetBerriesFoodModifierConfig {
    public final int nutritionAmount;
    public final float saturationAmount;

    public SweetBerriesFoodModifierConfig(FileConfiguration config) {
        this.nutritionAmount = config.getInt("nutrition-amount", 6);
        this.saturationAmount = (float) config.getDouble("saturation-amount", 0.4);
    }
}
