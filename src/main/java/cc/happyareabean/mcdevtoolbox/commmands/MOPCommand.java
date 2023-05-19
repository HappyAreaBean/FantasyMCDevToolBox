package cc.happyareabean.mcdevtoolbox.commmands;

import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.BukkitCommandActor;

public class MOPCommand {

    @Command("mop")
    public void mop(BukkitCommandActor actor) {
        actor.requirePlayer();

        Player player = actor.getAsPlayer();

        if (!player.isOp()) {
            player.setOp(true);
            actor.reply("&aYou are now OP!");
        } else {
            player.setOp(false);
            actor.reply("&cYou are no longer a OP!");
        }
    }

}
