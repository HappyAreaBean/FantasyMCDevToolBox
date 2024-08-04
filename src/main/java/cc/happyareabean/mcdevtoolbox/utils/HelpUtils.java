package cc.happyareabean.mcdevtoolbox.utils;

import cc.happyareabean.mcdevtoolbox.MCDevToolbox;
import revxrsal.commands.help.CommandHelp;

import java.util.ArrayList;
import java.util.List;

public class HelpUtils {

    public static List<String> buildCommandHelp(CommandHelp<String> helpEntries, int page, String subCommand) {
        if (subCommand != null) helpEntries.removeIf(s -> !s.split("-")[0].contains(subCommand));
        int slotPerPage = 5;
        int maxPages = helpEntries.getPageSize(slotPerPage);
        List<String> list = new ArrayList<>();
        list.add("&8&m----------------------------------------");
        list.add(String.format("&c&lFantasyMCDevToolBox &f(v%s) &7- &fPage &9(%s/%s)", MCDevToolbox.getVersion(), page, maxPages));
        list.add("&fMade With &4❤ &fBy HappyAreaBean");
        list.add("");
        list.addAll(helpEntries.paginate(page, slotPerPage));
        list.add("");
        list.add("&8&m----------------------------------------");
        return list;
    }

}
