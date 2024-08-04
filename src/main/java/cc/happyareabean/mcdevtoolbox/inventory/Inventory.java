package cc.happyareabean.mcdevtoolbox.inventory;

import de.exlll.configlib.annotation.ConfigurationElement;
import de.exlll.configlib.annotation.NoConvert;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

@ConfigurationElement
@Getter @Setter @AllArgsConstructor @NoArgsConstructor(force = true)
public class Inventory {

	private final String name;

	@NoConvert
	private List<ItemStack> contents = Arrays.asList(new ItemStack(Material.STONE), new ItemStack(Material.DIAMOND));

	@NoConvert
	private List<ItemStack> armor = Arrays.asList(new ItemStack(Material.IRON_HELMET), new ItemStack(Material.IRON_CHESTPLATE));

}
