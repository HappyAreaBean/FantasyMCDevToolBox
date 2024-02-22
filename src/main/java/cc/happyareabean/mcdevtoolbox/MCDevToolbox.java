package cc.happyareabean.mcdevtoolbox;

import cc.happyareabean.mcdevtoolbox.commmands.GMCommand;
import cc.happyareabean.mcdevtoolbox.commmands.MOPCommand;
import cc.happyareabean.mcdevtoolbox.commmands.ReadFromFileCommand;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.bukkit.BukkitCommandHandler;

import java.io.File;

@Getter
public class MCDevToolbox extends JavaPlugin {

    @Getter private static MCDevToolbox instance;
    private CommandHandler commandHandler;

    @Override
    public void onEnable() {
       instance = this;

       if (!getDataFolder().exists()) {
           getDataFolder().mkdir();
           new File(getDataFolder(), "items").mkdir();
       }

       commandHandler = BukkitCommandHandler.create(this);
       commandHandler.getAutoCompleter().registerSuggestion("itemFiles", new File(getDataFolder(), "items").list());
       commandHandler.register(new MOPCommand(), new GMCommand(), new ReadFromFileCommand());
    }

}