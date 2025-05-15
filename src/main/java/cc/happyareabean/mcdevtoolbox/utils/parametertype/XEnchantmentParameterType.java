package cc.happyareabean.mcdevtoolbox.utils.parametertype;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.base.XModule;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.exception.CommandErrorException;
import revxrsal.commands.node.ExecutionContext;
import revxrsal.commands.parameter.ParameterType;
import revxrsal.commands.stream.MutableStringStream;

public class XEnchantmentParameterType implements ParameterType<BukkitCommandActor, XEnchantment> {
    @Override
    public XEnchantment parse(@NotNull MutableStringStream mutableStringStream, @NotNull ExecutionContext<@NotNull BukkitCommandActor> executionContext) {
        return XEnchantment.REGISTRY.getByName(mutableStringStream.readString())
                .orElseThrow(() -> new CommandErrorException("Invalid enchantment."));
    }

    @Override
    public @NotNull SuggestionProvider<@NotNull BukkitCommandActor> defaultSuggestions() {
        return executionContext ->
                XEnchantment.REGISTRY.getValues().stream().map(XModule::name).toList();
    }
}
