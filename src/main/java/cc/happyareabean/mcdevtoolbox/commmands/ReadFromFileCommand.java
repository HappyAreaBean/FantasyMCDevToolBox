package cc.happyareabean.mcdevtoolbox.commmands;

import cc.happyareabean.mcdevtoolbox.MCDevToolbox;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.AutoComplete;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.annotation.Switch;
import revxrsal.commands.bukkit.BukkitCommandActor;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static revxrsal.commands.util.Strings.colorize;

@Command("readfromfile")
public class ReadFromFileCommand {

    @DefaultFor("~")
    @Subcommand("help")
    public void help(BukkitCommandActor actor) {
        actor.reply(colorize("&c/readfromfile <item> <file>"));
    }

    @SneakyThrows
    @Subcommand("item")
    @AutoComplete("@itemFiles")
    public void item(BukkitCommandActor actor, String fileName, @Switch("c") @Optional boolean clearInventory) {
        Player player = actor.requirePlayer();

        if (clearInventory) {
            actor.reply("&7&oClearing your inventory...");
            player.getInventory().clear();
        }

        File file = new File(MCDevToolbox.getInstance().getDataFolder(), "items/" + fileName);

        if (!file.exists()) {
            actor.error("The file [%s] does not exist.".formatted(fileName));
            return;
        }

        actor.reply(colorize("&aGenerate your items... &7[%s]".formatted(fileName)));
        List<String> strings = FileUtils.readLines(file, StandardCharsets.UTF_8);
        strings.forEach(string -> {

            player.performCommand("give %s %s".formatted(player.getName(), string));

        });
        actor.reply(colorize("&6Completed! &7[%s]".formatted(strings.size())));
    }

}
