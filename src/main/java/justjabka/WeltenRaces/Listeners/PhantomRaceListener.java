package justjabka.WeltenRaces.Listeners;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.tag.TagKey;
import justjabka.WeltenRaces.Configs.Race.PhantomConfig;
import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.Types.Race;
import justjabka.WeltenRaces.WeltenRaces;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PhantomRaceListener implements Listener {
    private final PhantomConfig config;

    public PhantomRaceListener(PhantomConfig config) {
        this.config = config;
    }

    private static final TagKey<ItemType> IS_MEAT = TagKey.create(RegistryKey.ITEM, Key.key(WeltenRaces.NAMESPACE, "is_meat"));

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();

        if (RaceManager.getRace(player) != Race.PHANTOM) return;

        Material consumedMaterial = event.getItem().getType();
        ItemType consumedType = consumedMaterial.asItemType();
        Registry<ItemType> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ITEM);

        if (consumedMaterial == Material.PHANTOM_MEMBRANE) {
            player.heal(config.membraneHealAmount, EntityRegainHealthEvent.RegainReason.EATING);
        } else if (registry.getTagValues(IS_MEAT).contains(consumedType)) {
            player.addPotionEffect(new PotionEffect(
                    PotionEffectType.SATURATION,
                    10,
                    0,
                    false,
                    false,
                    false
            ));
            player.addPotionEffect(new PotionEffect(
                    PotionEffectType.REGENERATION,
                    40,
                    0,
                    false,
                    false,
                    false
            ));
        }
    }
}
