package cc.happyareabean.mcdevtoolbox.commmands;

import cc.happyareabean.mcdevtoolbox.MCDevToolbox;
import cc.happyareabean.mcdevtoolbox.annotations.ItemFilesSuggestion;
import cc.happyareabean.mcdevtoolbox.utils.CommandUtils;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.CommandPlaceholder;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Range;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.annotation.Switch;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.help.Help;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Command("readfromfile")
public class ReadFromFileCommand {

    @CommandPlaceholder
    public void help(BukkitCommandActor actor,
                     @Range(min = 1) @Default("1") int page,
                     Help.RelatedCommands<BukkitCommandActor> commands) {
        CommandUtils.handleHelpMenu(actor, page, commands, 6, "readfromfile ");
    }

    @SneakyThrows
    @Subcommand("item")
    public void item(BukkitCommandActor actor, @ItemFilesSuggestion String fileName, @Switch("c") @Optional boolean clearInventory) {
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

        actor.reply("&aGenerate your items... &7[%s]".formatted(fileName));
        List<String> strings = FileUtils.readLines(file, StandardCharsets.UTF_8);
        strings.forEach(string -> {

            player.performCommand("give %s %s".formatted(player.getName(), string));

        });
        actor.reply("&6Completed! &7[%s]".formatted(strings.size()));
    }

}
