package cc.happyareabean.mcdevtoolbox.commmands;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.BukkitCommandActor;

public class GMCommand {

    @Command("gmc")
    public void gmc(BukkitCommandActor actor) {
        actor.requirePlayer();

        Player player = actor.getAsPlayer();
        player.setGameMode(GameMode.CREATIVE);
    }

    @Command("gms")
    public void gms(BukkitCommandActor actor) {
        actor.requirePlayer();

        Player player = actor.getAsPlayer();
        player.setGameMode(GameMode.SURVIVAL);
    }

    @Command("gma")
    public void gma(BukkitCommandActor actor) {
        actor.requirePlayer();

        Player player = actor.getAsPlayer();
        player.setGameMode(GameMode.ADVENTURE);
    }

    @Command("gmsp")
    public void gmsp(BukkitCommandActor actor) {
        actor.requirePlayer();

        Player player = actor.getAsPlayer();
        player.setGameMode(GameMode.SPECTATOR);
    }

}
