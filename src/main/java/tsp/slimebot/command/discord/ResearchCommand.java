package tsp.slimebot.command.discord;

import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import tsp.slimebot.util.Utils;

import java.util.Optional;

public class ResearchCommand implements SlimeCommand {

    @Override
    public String getName() {
        return "research";
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        String rawResearch = event.getOption("name").getAsString();

        Optional<Research> research = Slimefun.getRegistry().getResearches().stream()
                .filter(r -> Utils.matches(rawResearch, r))
                .findFirst();

        String result;
        if (research.isPresent()) {
            result = Utils.research(research.get());
        } else {
            result = "沒有這個研究鍵: " + Utils.wrap(rawResearch);
        }

        event.getHook().editOriginalEmbeds(Utils.embed(event)
                .setAuthor(research
                        .map(r -> r.getKey().getKey().toUpperCase())
                        .orElse("未知研究."))
                .appendDescription(result)
                .build()).queue();
    }
}