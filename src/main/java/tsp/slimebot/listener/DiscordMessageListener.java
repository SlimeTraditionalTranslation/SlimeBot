package tsp.slimebot.listener;

import io.github.thebusybiscuit.slimefun4.api.items.ItemState;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactive;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import tsp.slimebot.SlimeBot;
import tsp.slimebot.util.MessageUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class DiscordMessageListener extends ListenerAdapter {

    private final List<SlimefunItem> items = new ArrayList<>();

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        SlimeBot plugin = SlimeBot.getInstance();
        String prefix = plugin.getSettings().getPrefix();
        if (!args[0].startsWith(prefix)) {
            return;
        }
        if (items.isEmpty()) {
            items.addAll(Slimefun.getRegistry().getAllSlimefunItems());
        }

        String cmd = args[0];
        if (cmd.equalsIgnoreCase(prefix + "items")) {
            event.getChannel().sendMessage(new MessageBuilder()
                    .setEmbeds(new EmbedBuilder()
                            .setAuthor("Fetching...")
                            .appendDescription("Fetching the items! Please wait...")
                            .build())
                    .build()).queue(message -> {

                String result = "No results found.";
                String type = "all";
                if (args.length > 1) {
                    type = args[1];
                }

                if (type.equalsIgnoreCase("all")) {
                    result = MessageUtils.asString(items);
                } else if (type.equalsIgnoreCase("radioactive")) {
                    result = MessageUtils.asString(items.stream()
                            .filter(item -> item instanceof Radioactive)
                            .collect(Collectors.toList()));
                } else if (type.equalsIgnoreCase("enabled")) {
                    result = MessageUtils.asString(items.stream()
                            .filter(item -> !item.isDisabled())
                            .collect(Collectors.toList()));
                } else if (type.equalsIgnoreCase("disabled")) {
                    result = MessageUtils.asString(items.stream()
                            .filter(SlimefunItem::isDisabled)
                            .collect(Collectors.toList()));
                } else if (type.startsWith("a:")) {
                    String rawAddon = type.substring(2);
                    type = rawAddon;
                    Optional<Plugin> addon = Slimefun.getInstalledAddons().stream()
                            .filter(a -> a.getName().equalsIgnoreCase(rawAddon))
                            .findFirst();

                    if (addon.isPresent()) {
                        result = MessageUtils.asString(items.stream()
                                .filter(item -> item.getAddon().getName().equalsIgnoreCase(addon.get().getName()))
                                .collect(Collectors.toList()));
                    } else {
                        result = "No addon with the name: " + rawAddon;
                    }
                }

                message.editMessage(new MessageBuilder()
                        .setEmbeds(new EmbedBuilder()
                                .setAuthor(type.toUpperCase() + " Items | Requested by " + event.getAuthor().getAsTag())
                                .appendDescription(result.substring(0, Math.min(result.length(), 4096))) // Discord character limit
                                .build())
                        .build()).queue();
            });
            return;
        }

        if (cmd.equalsIgnoreCase(prefix + "item")) {
            if (args.length > 0) {
                StringBuilder builder = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    builder.append(args[i]);

                    if (i != args.length - 1) {
                        builder.append(" ");
                    }
                }
                String rawItem = builder.toString();

                event.getChannel().sendMessage(new MessageBuilder()
                        .setEmbeds(new EmbedBuilder()
                                .setTitle("Searching...")
                                .appendDescription("Searching for item: " + MessageUtils.wrap(rawItem))
                                .build())
                        .build()).queue(message -> {

                    for (SlimefunItem item : items) {
                        String name = ChatColor.stripColor(item.getItemName());
                        if (matches(rawItem, item)) {
                            String wiki = item.getWikipage().isPresent() ? item.getWikipage().get() : "";

                            message.editMessage(new MessageBuilder()
                                    .setEmbeds(new EmbedBuilder()
                                            .setAuthor(name, !wiki.isEmpty() ? wiki : "https://github.com/Slimefun/Slimefun4/wiki")
                                            .appendDescription("**Information**" + "\n")
                                            .appendDescription(MessageUtils.information(item))
                                            .appendDescription("\n")
                                            .appendDescription("**Description**" + "\n")
                                            .appendDescription(MessageUtils.description(item))
                                            .appendDescription("\n")
                                            .appendDescription("**Category**\n")
                                            .appendDescription(MessageUtils.category(item))
                                            .appendDescription("\n")
                                            .appendDescription("**Research**\n")
                                            .appendDescription(MessageUtils.research(item))
                                            .appendDescription("\n")
                                            .appendDescription("**Recipe**" + "\n")
                                            .appendDescription(MessageUtils.recipe(item) + "\n")
                                            .appendDescription(MessageUtils.recipeGrid(item))

                                            .setFooter("Requested by " + event.getAuthor().getAsTag())
                                            .setTimestamp(Instant.now())
                                            .build())
                                    .build()).queue();
                            return;
                        }
                    }

                    message.editMessage(new MessageBuilder()
                            .setEmbeds(new EmbedBuilder()
                                    .setTitle("Invalid item.")
                                    .appendDescription("That item does not exist!" + "\n")
                                    .appendDescription("Example item: " + MessageUtils.wrap(MessageUtils.stripColor(items.get(ThreadLocalRandom.current().nextInt(items.size())).getItemName())))
                                    .build())
                            .build()).queue();
                });
            }
        }
    }

    private boolean matches(String raw, SlimefunItem provided) {
        String name = ChatColor.stripColor(provided.getItemName());
        if (raw.equalsIgnoreCase(provided.getId())) { // Attempt to match id
            return true;
        } else if (raw.equalsIgnoreCase(name)) { // Attempt to match name
            return true;
        } else if (raw.startsWith(name)) { // Attempt to match start of a name
            return true;
        }

        return false;
    }

}