package cc.happyareabean.mcdevtoolbox.inventory;

import cc.happyareabean.mcdevtoolbox.MCDevToolbox;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.command.ExecutableCommand;

import java.util.Collection;
import java.util.List;

public class InventorySuggestionProvider implements SuggestionProvider {

    @Override
    public @NotNull Collection<String> getSuggestions(@NotNull List<String> args, @NotNull CommandActor sender, @NotNull ExecutableCommand command) throws Throwable {
        return MCDevToolbox.getInstance().getInventory().getInventory().keySet();
    }

}
