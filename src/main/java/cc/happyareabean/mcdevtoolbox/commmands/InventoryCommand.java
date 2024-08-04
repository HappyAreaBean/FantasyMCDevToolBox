package cc.happyareabean.mcdevtoolbox.commmands;

import cc.happyareabean.mcdevtoolbox.MCDevToolbox;
import cc.happyareabean.mcdevtoolbox.inventory.Inventory;
import cc.happyareabean.mcdevtoolbox.inventory.InventoryConfig;
import cc.happyareabean.mcdevtoolbox.inventory.MCDInventory;
import cc.happyareabean.mcdevtoolbox.utils.CC;
import cc.happyareabean.mcdevtoolbox.utils.HelpUtils;
import cc.happyareabean.paste.PasteFactory;
import org.apache.commons.io.FileUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import revxrsal.commands.annotation.AutoComplete;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.help.CommandHelp;

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

	@DefaultFor("~")
	public void inv(BukkitCommandActor actor, CommandHelp<String> helpEntries, @Optional @Default("1") int page) {
		HelpUtils.buildCommandHelp(helpEntries, page, null)
				.forEach(actor::reply);
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
		actor.reply("&a'%s' inventory has been successfully reloaded!".formatted(inventory.getInventory().size()));
	}

	@Subcommand("save")
	public void save(BukkitCommandActor actor, String kitName) {
		Player player = actor.requirePlayer();
		MCDInventory inventory = MCDevToolbox.getInstance().getInventory();

		List<ItemStack> inv = Arrays.stream(player.getInventory().getContents()).map(i -> (i == null) ? new ItemStack(Material.AIR) : i).collect(Collectors.toList());
		List<ItemStack> armor = Arrays.stream(player.getInventory().getArmorContents()).map(i -> (i == null) ? new ItemStack(Material.AIR) : i).collect(Collectors.toList());

		inventory.add(kitName, new Inventory(kitName, inv, armor));

		actor.reply(String.format("&aInventory &f'%s' &ahas been saved successfully!", kitName));
	}

	@Subcommand("delete")
	public void delete(BukkitCommandActor actor, Inventory inventory) {
		MCDInventory mcdInventory = MCDevToolbox.getInstance().getInventory();

		if (mcdInventory.delete(inventory)) {
			actor.reply(String.format("&cInventory '&f%s' &chas been delete successfully.", inventory));
		} else {
			actor.error(String.format("&cSomething went wrong while delete inventory '%s'...", inventory));
		}
	}

	@Subcommand("load")
	public void load(BukkitCommandActor actor, Inventory inventory) {
		Player player = actor.requirePlayer();
		MCDInventory mcdInventory = MCDevToolbox.getInstance().getInventory();

		player.getInventory().setContents(inventory.getContents().toArray(new ItemStack[0]));
		player.getInventory().setArmorContents(inventory.getArmor().toArray(new ItemStack[0]));

		actor.reply(String.format("&aInventory &f%s&a's &acontents and armor applied successfully!", inventory));
	}

	@Subcommand("download")
	public void download(BukkitCommandActor actor, String kitName, String url) {
		MCDInventory inventory = MCDevToolbox.getInstance().getInventory();
		actor.reply("&aDownloading inventory '%s' from '%s'...".formatted(kitName, url));

		File temp = new File(inventory.getFolder(), "temp_%s.yml".formatted(kitName));
		File file = new File(inventory.getFolder(), "%s.yml".formatted(kitName));

		try {
			FileUtils.copyURLToFile(new URL(url), temp);

			InventoryConfig config = new InventoryConfig(temp.toPath());
			config.load();

			FileUtils.moveFile(temp, file);

			inventory.add(kitName, new Inventory(kitName, config.getInventory().getContents(), config.getInventory().getArmor()));

			actor.reply("&aInventory '%s' has been successfully downloaded and saved!".formatted(kitName));
		} catch (Throwable e) {
			actor.reply("&cFailed to download from %s: %s".formatted(url, e.getMessage()));
			e.printStackTrace();
		}
	}

	@Subcommand("upload")
	@AutoComplete("@mcdInventory")
	public void upload(BukkitCommandActor actor, String kitName) {
		MCDInventory mcdInventory = MCDevToolbox.getInstance().getInventory();
		File file = mcdInventory.getInventoryFile(kitName);

		if (!file.exists()) {
			actor.dispatch("finv list");
			return;
		}

		CompletableFuture.runAsync(() -> {
			PasteFactory paste = PasteFactory.create("https://bytebin.happyareabean.cc");
			try {
				String key = paste.write(Files.readString(file.toPath()), "text/yaml");

				actor.reply("&aInventory &f%s &ahas been uploaded!".formatted(kitName));
				actor.reply("Raw   - https://bytebin.happyareabean.cc/" + key);
				actor.reply("Paste - https://paste.happyareabean.cc/" + key);
			} catch (Throwable e) {
				actor.reply("&cFailed to upload: " + e.getMessage());
			}
		});
	}

}
