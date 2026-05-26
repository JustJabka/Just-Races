package justjabka.JustRaces.DataProvider;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import justjabka.JustRaces.JustRacesAPI;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.bukkit.damage.DamageType;
import org.jetbrains.annotations.NotNull;

public class DamageTypeProvider {
    public static final DamageType ABSOLUTE_DAMAGE = getDamageType("absolute_damage");

    @NotNull
    private static DamageType getDamageType(@NotNull @KeyPattern.Value String key) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.DAMAGE_TYPE).getOrThrow(Key.key(JustRacesAPI.NAMESPACE, key));
    }
}