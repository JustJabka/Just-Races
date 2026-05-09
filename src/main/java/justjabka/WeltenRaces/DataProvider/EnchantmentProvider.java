package justjabka.WeltenRaces.DataProvider;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import justjabka.WeltenRaces.WeltenRaces;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

public class EnchantmentProvider {
    public static final Enchantment POISON = getEnchantment("poison");

    @NotNull
    private static Enchantment getEnchantment(@NotNull @KeyPattern.Value String key) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).getOrThrow(Key.key(WeltenRaces.NAMESPACE, key));
    }
}
