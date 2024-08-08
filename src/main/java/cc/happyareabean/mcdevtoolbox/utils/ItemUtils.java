package cc.happyareabean.mcdevtoolbox.utils;

import com.cryptomorin.xseries.reflection.XReflection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtils {

    public static boolean isUnbreakable(ItemStack itemStack) {
        final ItemMeta meta = itemStack.getItemMeta();

        if (XReflection.supports(10)) {
            return meta.isUnbreakable();
        } else {
            return NBTEditor.getBoolean(itemStack, "Unbreakable");
        }

    }
}
