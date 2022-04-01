package tsp.slimebot.command.discord;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
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
            builder.append("\n> Description: " + fromNullable(description.getDescription()));
            builder.append("\n> Version: " + fromNullable(description.getVersion()));
            builder.append("\n> Authors: " + fromNullable(String.join(", ", description.getAuthors())));
            builder.append("\n> Website: " + fromNullable(description.getWebsite()));

            if (detailed) {
                builder.append("\n> Main: " + fromNullable(description.getMain()));
                builder.append("\n> API Version: " + fromNullable(description.getAPIVersion()));
                builder.append("\n> Load: " + fromNullable(description.getLoad().name()));
                builder.append("\n> Libraries: " + fromNullable(description.getLibraries().toString()));
            }

            builder.append("\n\n");
        }

        event.getHook().editOriginalEmbeds(Utils.embed(event).setAuthor("Installed Addons (" + addons.size() + ") | Page: " + page)
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
