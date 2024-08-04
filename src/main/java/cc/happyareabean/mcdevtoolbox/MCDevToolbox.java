package cc.happyareabean.mcdevtoolbox;

import cc.happyareabean.mcdevtoolbox.commmands.EnchantItemCommand;
import cc.happyareabean.mcdevtoolbox.commmands.GMCommand;
import cc.happyareabean.mcdevtoolbox.commmands.InventoryCommand;
import cc.happyareabean.mcdevtoolbox.commmands.MOPCommand;
import cc.happyareabean.mcdevtoolbox.commmands.ReadFromFileCommand;
import cc.happyareabean.mcdevtoolbox.inventory.Inventory;
import cc.happyareabean.mcdevtoolbox.inventory.InventorySuggestionProvider;
import cc.happyareabean.mcdevtoolbox.inventory.MCDInventory;
import cc.happyareabean.mcdevtoolbox.utils.CC;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.bukkit.BukkitCommandHandler;
import revxrsal.commands.exception.CommandErrorException;

import java.io.File;

@Getter
public class MCDevToolbox extends JavaPlugin {

    public static boolean DEBUG = false;
    @Getter
    private static MCDevToolbox instance;
    private CommandHandler commandHandler;
    private MCDInventory inventory;

    @Override
    public void onEnable() {
        instance = this;

        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
            new File(getDataFolder(), "items").mkdir();
        }

        inventory = new MCDInventory(this);
        inventory.init();

        commandHandler = BukkitCommandHandler.create(this);
        commandHandler.getAutoCompleter().registerSuggestion("itemFiles", new File(getDataFolder(), "items").list());
        commandHandler.getAutoCompleter().registerSuggestion("mcdInventory", new InventorySuggestionProvider());
        commandHandler.getAutoCompleter().registerParameterSuggestions(Inventory.class, new InventorySuggestionProvider());
        commandHandler.setHelpWriter((command, actor) -> CC.translate("&8&l• &e" + String.format("/%s %s", command.getPath().toRealString(), command.getUsage())));
        commandHandler.registerValueResolver(Inventory.class, context -> {
            String inventoryName = context.pop();

            if (!inventory.getInventory().containsKey(inventoryName)) {
                throw new CommandErrorException("Inventory '%s' does not exist!");
            }

            return inventory.getInventory().get(inventoryName);
        });

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