package cc.happyareabean.mcdevtoolbox;

import cc.happyareabean.mcdevtoolbox.annotations.ItemFilesSuggestion;
import cc.happyareabean.mcdevtoolbox.annotations.MCDInventorySuggestion;
import cc.happyareabean.mcdevtoolbox.commmands.EnchantItemCommand;
import cc.happyareabean.mcdevtoolbox.commmands.GMCommand;
import cc.happyareabean.mcdevtoolbox.commmands.InventoryCommand;
import cc.happyareabean.mcdevtoolbox.commmands.MOPCommand;
import cc.happyareabean.mcdevtoolbox.commmands.ReadFromFileCommand;
import cc.happyareabean.mcdevtoolbox.inventory.Inventory;
import cc.happyareabean.mcdevtoolbox.inventory.MCDInventory;
import cc.happyareabean.mcdevtoolbox.utils.parametertype.XEnchantmentParameterType;
import cc.happyareabean.mcdevtoolbox.utils.suggestions.InventorySuggestionProvider;
import cc.happyareabean.mcdevtoolbox.utils.suggestions.ItemFilesSuggestionProvider;
import com.cryptomorin.xseries.XEnchantment;
import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.Lamp;
import revxrsal.commands.bukkit.BukkitLamp;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.exception.CommandErrorException;

import java.io.File;

@Getter
public class MCDevToolbox extends JavaPlugin {

    public static boolean DEBUG = false;
    @Getter
    private static MCDevToolbox instance;
    private BukkitAudiences audiences;
    private Lamp<BukkitCommandActor> commandHandler;
    private MCDInventory inventory;

    @Override
    public void onEnable() {
        instance = this;
        audiences = BukkitAudiences.create(this);

        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
            new File(getDataFolder(), "items").mkdir();
        }

        inventory = new MCDInventory(this);
        inventory.init();

        var command = BukkitLamp.builder(this);
        command.suggestionProviders(builder -> {
            builder.addProviderForAnnotation(ItemFilesSuggestion.class, suggestion -> new ItemFilesSuggestionProvider());
            builder.addProviderForAnnotation(MCDInventorySuggestion.class, provider -> new InventorySuggestionProvider());
            builder.addProvider(Inventory.class, new InventorySuggestionProvider());
        });
        command.parameterTypes(builder -> {
            builder.addParameterType(Inventory.class, (stream, context) -> {
                String inventoryName = stream.readString();

                if (!inventory.getInventory().containsKey(inventoryName)) {
                    throw new CommandErrorException("Inventory '%s' does not exist!".formatted(inventoryName));
                }

                return inventory.getInventory().get(inventoryName);
            });
            builder.addParameterType(XEnchantment.class, new XEnchantmentParameterType());
        });

        commandHandler = command.build();
        commandHandler.register(
                new MOPCommand(),
                new GMCommand(),
                new ReadFromFileCommand(),
                new InventoryCommand(),
                new EnchantItemCommand()
        );
    }

    public static String getVersion() {
        return instance.getDescription().getVersion();
    }

}