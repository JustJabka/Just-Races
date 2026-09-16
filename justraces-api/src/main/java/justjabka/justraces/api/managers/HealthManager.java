package justjabka.justraces.api.managers;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

public final class HealthManager {

    private HealthManager() {}

    public static double getLostHealthPercent(Player player) {
        double currentHealth = player.getHealth();
        double maxHealth = getMaxHealth(player);
        double lostHealth = maxHealth - currentHealth;

        return Math.clamp(lostHealth / maxHealth, 0.0, 1.0);
    }

    public static double getCurrentHealthPercent(Player player) {
        double currentHealth = player.getHealth();
        double maxHealth = getMaxHealth(player);

        return Math.clamp(currentHealth / maxHealth, 0.0, 1.0);
    }

    public static double getMaxHealth(Player player) {
        AttributeInstance maxHealthInstance = player.getAttribute(Attribute.MAX_HEALTH);
        if (maxHealthInstance == null) return 0;

        return maxHealthInstance.getValue();
    }

    public static void setHealthPercent(Player player, double percent) {
        setHealthPercent(player, percent, false);
    }

    public static void setHealthPercent(Player player, double percent, boolean canBeLethal) {
        double maxHealth = getMaxHealth(player);

        double clampedPercent = Math.clamp(percent, 0.0, 1.0);

        double finalHealth = Math.min(maxHealth * clampedPercent, maxHealth);

        if (canBeLethal && finalHealth <= 0) {
            player.setHealth(0.1);
        } else {
            player.setHealth(maxHealth * percent);
        }
    }
}
