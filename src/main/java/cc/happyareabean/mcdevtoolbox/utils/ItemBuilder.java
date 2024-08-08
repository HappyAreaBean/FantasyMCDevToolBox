package cc.happyareabean.mcdevtoolbox.utils;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.profiles.builder.XSkull;
import com.cryptomorin.xseries.profiles.objects.Profileable;
import com.cryptomorin.xseries.reflection.XReflection;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemBuilder implements Listener, Cloneable {

    private ItemStack itemStack;

    public ItemBuilder(final Material mat) {
        itemStack = new ItemStack(mat);
    }

    public ItemBuilder(final ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemBuilder amount(final int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder name(final String name) {
        final ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(final Iterable<String> lore) {
        final ItemMeta meta = itemStack.getItemMeta();

        List<String> toSet = meta.getLore();
        if (toSet == null) {
            toSet = new ArrayList<>();
        }

        for (final String string : lore) {
            toSet.add(ChatColor.translateAlternateColorCodes('&', string));
        }

        meta.setLore(toSet);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(final String... lore) {
        final ItemMeta meta = itemStack.getItemMeta();

        List<String> toSet = meta.getLore();
        if (toSet == null) {
            toSet = new ArrayList<>();
        }

        for (final String string : lore) {
            toSet.add(ChatColor.translateAlternateColorCodes('&', string));
        }

        meta.setLore(toSet);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder durability(final int durability) {
        itemStack.setDurability((short) - (durability - itemStack.getType().getMaxDurability()));
        return this;
    }

    public ItemBuilder data(final int data) {
        itemStack.setDurability((short) data);
        return this;
    }

    /**
     * <p>Set unbreakable value.</p>
     * <p><b>Please note that this does not work if your server version is higher 1.8.</b></p>
     * @param unbreakable true if unbreakable, false if not unbreakable
     * @return ItemBuilder
     */
    public ItemBuilder unbreakable(boolean unbreakable) {
        final ItemMeta meta = itemStack.getItemMeta();

        if (XReflection.supports(10)) {
            meta.setUnbreakable(!meta.isUnbreakable());
        } else {
            itemStack = NBTEditor.set(itemStack, unbreakable, "Unbreakable");
            return this;
        }

        itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * <p>Reverse unbreakable value.</p>
     * <p>e.g. if true will set to false</p>
     * <p><b>Please note that this does not work if your server version is higher 1.8.</b></p>
     * @return ItemBuilder
     */
    public ItemBuilder unbreakable() {
        final ItemMeta meta = itemStack.getItemMeta();

        if (XReflection.supports(10)) {
            meta.setUnbreakable(!meta.isUnbreakable());
        } else {
            boolean unbreakable = NBTEditor.getBoolean(itemStack, "Unbreakable");

            itemStack = NBTEditor.set(itemStack, !unbreakable, "Unbreakable");
            return this;
        }

        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder enchantment(final Enchantment enchantment, final int level) {
        itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder enchantment(final Enchantment enchantment) {
        itemStack.addUnsafeEnchantment(enchantment, 1);
        return this;
    }

    public ItemBuilder type(final Material material) {
        itemStack.setType(material);
        return this;
    }

    public ItemBuilder clearLore() {
        final ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(new ArrayList<String>());
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder clearEnchantments() {
        for (final Enchantment e : itemStack.getEnchantments().keySet()) {
            itemStack.removeEnchantment(e);
        }
        return this;
    }

    public ItemBuilder color(final Color color) {
        if (itemStack.getType() == Material.LEATHER_BOOTS || itemStack.getType() == Material.LEATHER_CHESTPLATE || itemStack.getType() == Material.LEATHER_HELMET
                || itemStack.getType() == Material.LEATHER_LEGGINGS) {
            final LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
            meta.setColor(color);
            itemStack.setItemMeta(meta);
            return this;
        } else
            throw new IllegalArgumentException("color() only applicable for leather armor!");
    }

    public ItemBuilder head(UUID playerUUID) {
        ItemMeta meta = XSkull.of(itemStack.getItemMeta()).profile(Profileable.of(playerUUID)).apply();
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder headTexture(String texture) {
        if (texture != null) {
            SkullMeta hm = (SkullMeta) itemStack.getItemMeta();
            GameProfile profile = new GameProfile(new UUID(texture.hashCode(), texture.hashCode()), null);
            profile.getProperties().put("textures", new Property("Value", texture));

            try{
                Field profileField = hm.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(hm, profile);
            } catch(NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }

            itemStack.setItemMeta(hm);
        }
        return this;
    }

    public ItemBuilder skull(String owner) {
        try {
            SkullMeta im = (SkullMeta) itemStack.getItemMeta();
            im.setOwner(owner);
            itemStack.setItemMeta(im);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return this;
    }

    public ItemBuilder shiny() {
        ItemMeta meta = this.itemStack.getItemMeta();

        meta.addEnchant(XEnchantment.UNBREAKING.getEnchant(), 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder itemFlag(ItemFlag itemFlag) {
        ItemMeta im = itemStack.getItemMeta();
        im.addItemFlags(itemFlag);
        itemStack.setItemMeta(im);
        return this;
    }

    public ItemBuilder removeItemFlag(ItemFlag itemFlag) {
        ItemMeta im = itemStack.getItemMeta();
        if (im.hasItemFlag(itemFlag)) {
            im.removeItemFlags(itemFlag);
        }
        itemStack.setItemMeta(im);
        return this;
    }

    @Override
    public ItemBuilder clone() {
        return new ItemBuilder(this.itemStack.clone());
    }

    public ItemStack build() {
        return itemStack;
    }

    public Material getType() {
        return this.itemStack.getType();
    }

}
