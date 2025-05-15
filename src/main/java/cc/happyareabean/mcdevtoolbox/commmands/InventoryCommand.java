package cc.happyareabean.mcdevtoolbox.commmands;

import cc.happyareabean.mcdevtoolbox.MCDevToolbox;
import cc.happyareabean.mcdevtoolbox.annotations.MCDInventorySuggestion;
import cc.happyareabean.mcdevtoolbox.inventory.Inventory;
import cc.happyareabean.mcdevtoolbox.inventory.InventoryConfig;
import cc.happyareabean.mcdevtoolbox.inventory.MCDInventory;
import cc.happyareabean.mcdevtoolbox.utils.CC;
import cc.happyareabean.mcdevtoolbox.utils.CommandUtils;
import cc.happyareabean.paste.PasteFactory;
import org.apache.commons.io.FileUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.CommandPlaceholder;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Range;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.help.Help;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@Command({"inv", "finv"})
public class InventoryCommand {

	@CommandPlaceholder
	public void help(BukkitCommandActor actor,
					 @Range(min = 1) @Default("1") int page,
					 Help.RelatedCommands<BukkitCommandActor> commands) {
		CommandUtils.handleHelpMenu(actor, page, commands, 8, "finv ");
	}

	@Subcommand("list")
	public void list(BukkitCommandActor actor) {
		MCDInventory inventory = MCDevToolbox.getInstance().getInventory();
		actor.reply(CC.translate("&cAvailable Inventory: &f") +
				(inventory.getInventory().isEmpty() ? "None" : inventory.getInventory().keySet().stream().sorted().collect(Collectors.joining(", "))));
	}

	@Subcommand("reload")
	public void reload(BukkitCommandActor actor) {
		MCDInventory inventory = MCDevToolbox.getInstance().getInventory();

		inventory.loadInventory();

		actor.reply("&a%s inventory has been successfully reloaded!".formatted(inventory.getInventory().size()));
	}

	@Subcommand("save")
	public void save(BukkitCommandActor actor, String invName) {
		Player player = actor.requirePlayer();
		MCDInventory inventory = MCDevToolbox.getInstance().getInventory();

		List<ItemStack> inv = Arrays.stream(player.getInventory().getContents()).map(i -> (i == null) ? new ItemStack(Material.AIR) : i).collect(Collectors.toList());
		List<ItemStack> armor = Arrays.stream(player.getInventory().getArmorContents()).map(i -> (i == null) ? new ItemStack(Material.AIR) : i).collect(Collectors.toList());

		inventory.add(invName, new Inventory(invName, inv, armor));

		actor.reply(String.format("&aInventory &f'%s' &ahas been saved successfully!", invName));
	}

	@Subcommand("delete")
	public void delete(BukkitCommandActor actor, Inventory inventory) {
		MCDInventory mcdInventory = MCDevToolbox.getInstance().getInventory();

		if (mcdInventory.delete(inventory)) {
			actor.reply(String.format("&cInventory '&f%s' &chas been delete successfully.", inventory.getName()));
		} else {
			actor.error(String.format("&cSomething went wrong while delete inventory '%s'...", inventory.getName()));
		}
	}

	@Subcommand("load")
	public void load(BukkitCommandActor actor, Inventory inventory) {
		Player player = actor.requirePlayer();
		MCDInventory mcdInventory = MCDevToolbox.getInstance().getInventory();

		player.getInventory().setContents(inventory.getContents().toArray(new ItemStack[0]));
		player.getInventory().setArmorContents(inventory.getArmor().toArray(new ItemStack[0]));

		actor.reply(String.format("&aInventory &f%s&a's &acontents and armor applied successfully!", inventory.getName()));
	}

	@Subcommand("download")
	public void download(BukkitCommandActor actor, String invName, String url) {
		MCDInventory inventory = MCDevToolbox.getInstance().getInventory();
		actor.reply("&aDownloading inventory '%s' from '%s'...".formatted(invName, url));

		File temp = new File(inventory.getFolder(), "temp_%s.yml".formatted(invName));
		File file = new File(inventory.getFolder(), "%s.yml".formatted(invName));

		try {
			FileUtils.copyURLToFile(new URL(url), temp);

			InventoryConfig config = new InventoryConfig(temp.toPath());
			config.load();

			FileUtils.moveFile(temp, file);

			inventory.add(invName, new Inventory(invName, config.getInventory().getContents(), config.getInventory().getArmor()));

			actor.reply("&aInventory '%s' has been successfully downloaded and saved!".formatted(invName));
		} catch (Throwable e) {
			actor.reply("&cFailed to download from %s: %s".formatted(url, e.getMessage()));
			e.printStackTrace();
		}
	}

	@Subcommand("upload")
	public void upload(BukkitCommandActor actor, @MCDInventorySuggestion String invName) {
		MCDInventory mcdInventory = MCDevToolbox.getInstance().getInventory();
		File file = mcdInventory.getInventoryFile(invName);

		if (!file.exists()) {
			this.list(actor);
			return;
		}

		CompletableFuture.runAsync(() -> {
			PasteFactory paste = PasteFactory.create("https://bytebin.happyareabean.cc");
			try {
				String key = paste.write(Files.readString(file.toPath()), "text/yaml");

				actor.reply("&aInventory &f%s &ahas been uploaded!".formatted(invName));
				actor.reply("Raw   - https://bytebin.happyareabean.cc/" + key);
				actor.reply("Paste - https://paste.happyareabean.cc/" + key);
			} catch (Throwable e) {
				actor.reply("&cFailed to upload: " + e.getMessage());
			}
		});
	}

}
