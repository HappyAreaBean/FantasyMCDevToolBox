package cc.happyareabean.mcdevtoolbox.commmands;

import cc.happyareabean.mcdevtoolbox.utils.ItemBuilder;
import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.BukkitCommandActor;

public class EnchantItemCommand {

    @Command("enchantitem")
    public void enchantItem(BukkitCommandActor actor, XEnchantment enchantment, int level) {
        Player player = actor.requirePlayer();

        ItemStack stack = player.getItemInHand();

        if (stack.getType() == XMaterial.AIR.parseMaterial()) {
            actor.error("You can't enchant air.");
            return;
        }

        ItemBuilder builder = new ItemBuilder(stack);
        builder.enchantment(enchantment.getEnchant(), level);

        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), builder.build());

        actor.reply("&e[ToolBox] Added level &6'%s' &eenchantment &a'%s' &eto your &b'%s'.".formatted(level, enchantment.name(),
                XMaterial.matchXMaterial(stack).name()));
    }

}
