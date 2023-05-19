package cc.happyareabean.mcdevtoolbox;

import cc.happyareabean.mcdevtoolbox.commmands.GMCommand;
import cc.happyareabean.mcdevtoolbox.commmands.MOPCommand;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.bukkit.BukkitCommandHandler;

@Getter
public class MCDevToolbox extends JavaPlugin {

    private static MCDevToolbox instance;
    private CommandHandler commandHandler;

    @Override
    public void onEnable() {
       instance = this;

       commandHandler = BukkitCommandHandler.create(this);
       commandHandler.register(new MOPCommand(), new GMCommand());
    }

}