package tsp.slimebot.command.discord;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.mini2Dx.gettext.GetText;
import tsp.slimebot.util.Utils;

import java.util.Arrays;
import java.util.List;

public class AddonsCommand extends SlimeCommand {

    public AddonsCommand() {
        super("addons");
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
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
            builder.append("\n" + GetText.tr("> Description: ") + fromNullable(description.getDescription()));
            builder.append("\n" + GetText.tr("> Version: ") + fromNullable(description.getVersion()));
            builder.append("\n" + GetText.tr("> Authors: ") + fromNullable(String.join(GetText.tr(", "), description.getAuthors())));
            builder.append("\n" + GetText.tr("> Website: ") + fromNullable(description.getWebsite()));

            if (detailed) {
                builder.append("\n" + GetText.tr("> Main: ") + fromNullable(description.getMain()));
                builder.append("\n" + GetText.tr("> API Version: ") + fromNullable(description.getAPIVersion()));
                builder.append("\n" + GetText.tr("> Load: ") + fromNullable(description.getLoad().name()));
                builder.append("\n" + GetText.tr("> Libraries: ") + fromNullable(description.getLibraries().toString()));
            }

            builder.append("\n\n");
        }

        event.getHook().editOriginalEmbeds(Utils.embed(event).setAuthor(GetText.tr("Installed Addons (") + addons.size() + GetText.tr(") | Page: ") + page)
                .appendDescription(builder.toString())
                .build()).queue();
    }

    private String fromNullable(String s) {
        if (s == null) {
            return Utils.wrap(GetText.tr("None"));
        }

        return Utils.wrap(s);
    }

}
