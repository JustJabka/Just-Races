package justjabka.JustRaces.Configs.Modifier.Food;

import org.bukkit.configuration.file.FileConfiguration;

public class GlowBerriesFoodModifierConfig {
    public final int nutritionAmount;
    public final float saturationAmount;

    public GlowBerriesFoodModifierConfig(FileConfiguration config) {
        this.nutritionAmount = config.getInt("nutrition-amount", 6);
        this.saturationAmount = (float) config.getDouble("saturation-amount", 0.4);
    }
}
