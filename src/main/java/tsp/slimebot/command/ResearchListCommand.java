package tsp.slimebot.command;

import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import tsp.slimebot.util.Utils;
import tsp.slimebot.util.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class ResearchListCommand implements SlimeCommand {

    @Override
    public String getName() {
        return "researches";
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        OptionMapping pageOption = event.getOption("page");
        int page = 1;
        if (pageOption != null) {
            page = (int) pageOption.getAsLong();
        }
        List<Research> researches = Slimefun.getRegistry().getResearches().stream()
                .filter(research -> research != null)
                .collect(Collectors.toList());

        String result = Utils.asString(Utils.getPage(researches, page, 10));

        event.getHook().editOriginalEmbeds(Utils.embed(event)
                .setAuthor("Researches | Page: " + page)
                .appendDescription(result.substring(0, Math.min(result.length(), 4096)))
                .build())
                .queue();
    }

}
