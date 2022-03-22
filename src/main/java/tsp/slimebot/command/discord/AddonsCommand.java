package tsp.slimebot.command.discord;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import tsp.slimebot.util.Utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AddonsCommand implements SlimeCommand {

    @Override
    public String getName() {
        return "addons";
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        OptionMapping pageOption = event.getOption("page");
        int page = 1;
        if (pageOption != null) {
            page = (int) pageOption.getAsLong();
        }

        boolean detailed = event.getOption("detailed") != null ? event.getOption("detailed").getAsBoolean() : false;
        List<Plugin> addons = Utils.getPage(Arrays.asList(Slimefun.getInstalledAddons().toArray(new Plugin[0])), page, 5);

        StringBuilder builder = new StringBuilder();
        for (Plugin plugin : addons) {
            PluginDescriptionFile description = plugin.getDescription();

            builder.append("**" + plugin.getName() + "**");
            builder.append("\n> 介紹: " + fromNullable(description.getDescription()));
            builder.append("\n> 版本: " + fromNullable(description.getVersion()));
            builder.append("\n> 作者: " + fromNullable(String.join(", ", description.getAuthors())));
            builder.append("\n> 網站: " + fromNullable(description.getWebsite()));

            if (detailed) {
                builder.append("\n> 主要: " + fromNullable(description.getMain()));
                builder.append("\n> API版本: " + fromNullable(description.getAPIVersion()));
                builder.append("\n> 加載: " + fromNullable(description.getLoad().name()));
                builder.append("\n> 函式庫: " + fromNullable(description.getLibraries().toString()));
            }

            builder.append("\n\n");
        }

        event.getHook().editOriginalEmbeds(Utils.embed(event).setAuthor("已安裝的附加 (" + addons.size() + ") | 頁面: " + page)
                .appendDescription(builder.toString())
                .build()).queue();
    }

    private String fromNullable(String s) {
        if (s == null) {
            return Utils.wrap("None");
        }

        return Utils.wrap(s);
    }

}
