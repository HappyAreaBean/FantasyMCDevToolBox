package cc.happyareabean.mcdevtoolbox.utils;

import cc.happyareabean.mcdevtoolbox.MCDevToolbox;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.Nullable;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.command.ExecutableCommand;
import revxrsal.commands.help.Help;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static net.kyori.adventure.text.Component.text;

public class CommandUtils {

    private static final MiniMessage MM = MiniMessage.miniMessage();

    public static void handleHelpMenu(BukkitCommandActor actor,
                                      int page,
                                      Help.CommandList<BukkitCommandActor> commands,
                                      int elementsPerPage,
                                      @Nullable String filterFor) {
        List<ExecutableCommand<BukkitCommandActor>> list = new ArrayList<>();

        for (ExecutableCommand<BukkitCommandActor> command : commands.all()) {
            if (filterFor != null) {
                var usage = command.usage();
                if (!usage.startsWith(filterFor)) continue;
            }
            if (!command.permission().isExecutableBy(actor)) continue;
            list.add(command);
        }

        var componentList = new ArrayList<Component>();
        var commandList = Help.paginate(list, page, elementsPerPage);
        var numberOfPages = Help.numberOfPages(list.size(), elementsPerPage);

        var header = MM.deserialize("<dark_gray>========<gray>[ <gradient:#00C6FF:#0072FF><bold>FantasyMCDevToolBox</bold></gradient> <gray>]<dark_gray>========");
        var footer = MM.deserialize("<dark_gray>=========================================");

        componentList.add(header);
        componentList.add(Component.empty());
        commandList.forEach(command -> {
            var usage = command.usage();
            var description = command.description();
            var component = text()
                    .appendSpace()
                    .append(text("●", NamedTextColor.DARK_GRAY))
                    .appendSpace()
                    .append(text("/", NamedTextColor.YELLOW))
                    .append(text(usage, NamedTextColor.YELLOW)
                            .replaceText(builder -> {
                                builder.match(Pattern.compile("[<>\\[\\]]"))
                                        .replacement(new BiFunction<MatchResult, TextComponent.Builder, ComponentLike>() {
                                            @Override
                                            public ComponentLike apply(MatchResult matchResult, TextComponent.Builder builder) {
                                                return builder.color(NamedTextColor.GREEN);
                                            }
                                        });
                            })
                            .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + command.path()))
                            .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT,
                                    text(description != null ? description : "This command has no description.", NamedTextColor.GRAY))));
            componentList.add(component.build());
        });
        componentList.add(Component.empty());
        if (numberOfPages > 1) {
            componentList.add(paginateNavigation(page, numberOfPages, "/minesweeper %s"));
        } else {
            componentList.add(footer);
        }
        var result = Component.join(JoinConfiguration.newlines(), componentList);
        if (actor.audience().isPresent()) {
            actor.reply(result);
        } else {
            MCDevToolbox.getInstance().getAudiences().sender(actor.sender()).sendMessage(result);
        }
    }

    public static void handleHelpMenu(BukkitCommandActor actor, int page, Help.CommandList<BukkitCommandActor> commands, int elementsPerPage) {
        handleHelpMenu(actor, page, commands, elementsPerPage, null);
    }

    public static Component paginateNavigation(int currentPage, int maxPage, String commandFormat) {
        int previousPage = currentPage - 1;
        int nextPage = currentPage + 1;

        boolean havePreviousPage = previousPage != 0;
        boolean haveNextPage = maxPage != currentPage;

        TextComponent.Builder pageText = text()
                .color(NamedTextColor.GOLD);

        pageText.append(MM.deserialize("<reset><dark_gray>================<gray>[ </reset>"));

        pageText.append(text("⬅", !havePreviousPage ? NamedTextColor.DARK_GRAY : null)
                .decorate(TextDecoration.BOLD)
                .clickEvent(havePreviousPage ? ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, String.format(commandFormat, previousPage)) : null)
                .hoverEvent(havePreviousPage ? text(String.format("Page %s", previousPage)).color(NamedTextColor.GOLD) : null));

        pageText.append(text("  ▍  ").decorate(TextDecoration.BOLD));

        pageText.append(text("➡", !haveNextPage ? NamedTextColor.DARK_GRAY : null)
                .decorate(TextDecoration.BOLD)
                .clickEvent(haveNextPage ? ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, String.format(commandFormat, nextPage)) : null)
                .hoverEvent(haveNextPage ? text(String.format("Page %s", nextPage)).color(NamedTextColor.GOLD) : null));

        pageText.append(MM.deserialize(" <reset><gray>]<dark_gray>================</reset>"));

        return pageText.build();
    }

}
