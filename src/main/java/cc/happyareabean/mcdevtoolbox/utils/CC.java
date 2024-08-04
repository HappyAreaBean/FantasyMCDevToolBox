package cc.happyareabean.mcdevtoolbox.utils;

import org.bukkit.ChatColor;

import java.util.List;

public class CC {

    public static String translate(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static List<String> translate(List<String> input) {
        return input.parallelStream().map(CC::translate).toList();
    }

}
