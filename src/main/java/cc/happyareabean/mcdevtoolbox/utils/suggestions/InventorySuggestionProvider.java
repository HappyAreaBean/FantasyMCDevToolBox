package cc.happyareabean.mcdevtoolbox.utils.suggestions;

import cc.happyareabean.mcdevtoolbox.MCDevToolbox;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.node.ExecutionContext;

import java.util.Collection;

public class InventorySuggestionProvider implements SuggestionProvider<BukkitCommandActor> {

    @Override
    public @NotNull Collection<String> getSuggestions(@NotNull ExecutionContext executionContext) {
        return MCDevToolbox.getInstance().getInventory().getInventory().keySet();
    }

}
