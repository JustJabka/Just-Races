package justjabka.justraces.core.listeners;

import justjabka.justraces.api.JustRacesAPI;
import justjabka.justraces.api.common.definition.RaceDefinition;
import justjabka.justraces.api.managers.RaceManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingRecipe;

public class RecipeListener implements Listener {

    @EventHandler
    public void prepareCraft(PrepareItemCraftEvent event) {
        if (!(event.getView().getPlayer() instanceof Player player)) return;
        if (!(event.getRecipe() instanceof CraftingRecipe recipe)) return;

        // Allow if recipe is not race exclusive
        RaceDefinition race = RaceManager.getRace(player);
        if (!isRaceRecipe(recipe)) return;

        // Allow if race has this recipe
        if (race.hasRecipe(recipe)) return;

        event.getInventory().setResult(null);
    }

    private static boolean isRaceRecipe(CraftingRecipe recipe) {
        String namespace = recipe.getKey().getNamespace();
        return namespace.equals(JustRacesAPI.NAMESPACE);
    }
}
