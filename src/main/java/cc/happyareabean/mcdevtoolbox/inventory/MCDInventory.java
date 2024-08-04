package cc.happyareabean.mcdevtoolbox.inventory;

import cc.happyareabean.mcdevtoolbox.MCDevToolbox;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Getter
@Data
@RequiredArgsConstructor
public class MCDInventory {

    private Map<String, Inventory> inventory = new HashMap<>();
    private final MCDevToolbox plugin;
    private final String inventoryFolder = "inventory";
    private File folder;

    public void init() {
        folder = new File(plugin.getDataFolder(), inventoryFolder);
        if (!folder.exists()) folder.mkdir();

        loadInventory();
    }

    public void add(String inventoryName, Inventory inventory) {
        if (!this.inventory.containsKey(inventoryName))
            this.inventory.put(inventoryName, inventory);
        else
            this.inventory.replace(inventoryName, inventory);

        new InventoryConfig(getInventoryFile(inventoryName).toPath(), inventory).save();
    }

    public boolean delete(String inventoryName) {
        inventory.remove(inventoryName);

        return getInventoryFile(inventoryName).delete();
    }

    public boolean delete(Inventory inventory) {
        this.inventory.remove(inventory.getName());

        return getInventoryFile(inventory.getName()).delete();
    }

    public void loadInventory() {
        if (!folder.exists()) return;

        if (!inventory.isEmpty()) inventory.clear();

        File[] files = folder.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) continue;
            if (MCDevToolbox.DEBUG) plugin.getLogger().info("Loading Inventory: " + file.getName());

            String name = file.getName().replace(".yml", "");
            InventoryConfig inventoryConfig = new InventoryConfig(file.toPath());
            inventoryConfig.load();

            Inventory inventory = inventoryConfig.getInventory();

            this.inventory.put(name, inventory);
        }
    }

    public File getInventoryFile(String inventoryName) {
        return new File(folder, inventoryName + ".yml");
    }
}
