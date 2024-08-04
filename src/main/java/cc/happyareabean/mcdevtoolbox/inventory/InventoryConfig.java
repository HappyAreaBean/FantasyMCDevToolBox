package cc.happyareabean.mcdevtoolbox.inventory;

import de.exlll.configlib.annotation.ConfigurationElement;
import de.exlll.configlib.configs.yaml.BukkitYamlConfiguration;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;

@ConfigurationElement
@Getter @Setter
public class InventoryConfig extends BukkitYamlConfiguration {

	private Inventory inventory = new Inventory();

	public InventoryConfig(Path path) {
		super(path);
	}

	public InventoryConfig(Path path, Inventory inventory) {
		super(path);
		this.inventory = inventory;
	}
}
