package tsp.slimebot.command.discord;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactive;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.bukkit.plugin.Plugin;
import tsp.slimebot.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ItemListCommand implements SlimeCommand {

    @Override
    public String getName() {
        return "items";
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        OptionMapping pageMapping = event.getOption("page");
        OptionMapping typeMapping = event.getOption("type");

        String type = typeMapping != null ? typeMapping.getAsString() : "all";
        int page = 1;
        if (pageMapping != null) {
            page = (int) pageMapping.getAsLong();
        }

        List<SlimefunItem> items = Utils.getItems();
        List<?> results = new ArrayList<>();
        if (type.equalsIgnoreCase("all")) {
            results = Utils.getPage(items, page, 30);
        } else if (type.equalsIgnoreCase("radioactive")) {
            results = Utils.getPage(items.stream()
                    .filter(item -> item instanceof Radioactive)
                    .collect(Collectors.toList()), page, 30);
        } else if (type.equalsIgnoreCase("enabled")) {
            results = Utils.getPage(items.stream()
                    .filter(item -> !item.isDisabled())
                    .collect(Collectors.toList()), page, 30);
        } else if (type.equalsIgnoreCase("disabled")) {
            results = Utils.getPage(items.stream()
                    .filter(SlimefunItem::isDisabled)
                    .collect(Collectors.toList()), page, 30);
        } else if (type.startsWith("a:")) {
            String rawAddon = type.substring(2);
            type = rawAddon;
            Optional<Plugin> addon = Slimefun.getInstalledAddons().stream()
                    .filter(a -> a.getName().equalsIgnoreCase(rawAddon))
                    .findFirst();

            if (addon.isPresent()) {
                results = Utils.getPage(items.stream()
                        .filter(item -> item.getAddon().getName().equalsIgnoreCase(addon.get().getName()))
                        .collect(Collectors.toList()), page, 30);
            }
        } else if (type.startsWith("r:")) {
            String rawResearch = type.substring(2);
            type = rawResearch;
            Optional<Research> research = Slimefun.getRegistry().getResearches().stream()
                    .filter(r -> Utils.matches(rawResearch, r))
                    .findFirst();

            if (research.isPresent()) {
                results = Utils.getPage(research.get().getAffectedItems(), page, 30);
            }
        }

        String result = Utils.asString(results);

        event.getHook().editOriginalEmbeds(Utils.embed(event)
                .setAuthor(type.toUpperCase() + " 物品 | 頁面: " + page)
                .appendDescription(result.substring(0, Math.min(result.length(), 4096))) // Discord character limit
                .build()
        ).queue();
    }
}
