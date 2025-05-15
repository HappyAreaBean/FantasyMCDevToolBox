package cc.happyareabean.mcdevtoolbox.utils.suggestions;

import cc.happyareabean.mcdevtoolbox.MCDevToolbox;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.node.ExecutionContext;

import java.io.File;
import java.util.Collection;
import java.util.List;

public class ItemFilesSuggestionProvider implements SuggestionProvider<BukkitCommandActor> {

    @Override
    public @NotNull Collection<String> getSuggestions(@NotNull ExecutionContext executionContext) {
        return List.of(new File(MCDevToolbox.getInstance().getDataFolder(), "items").list());
    }

}
